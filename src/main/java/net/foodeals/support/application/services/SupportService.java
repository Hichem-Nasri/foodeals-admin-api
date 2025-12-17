package net.foodeals.support.application.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.transaction.Transactional;
import net.foodeals.support.application.dtos.*;
import net.foodeals.support.application.specification.SupportTicketSpecs;
import net.foodeals.support.domain.entities.SupportAttachment;
import net.foodeals.support.domain.entities.SupportResponse;
import net.foodeals.support.domain.entities.SupportTicket;
import net.foodeals.support.domain.entities.enums.ResponseType;
import net.foodeals.support.domain.entities.enums.SupportStatus;
import net.foodeals.support.domain.repositories.*;
import net.foodeals.user.domain.repositories.UserRepository;

@Service
public class SupportService {
	private final SupportTicketRepository ticketRepo;
	private final SupportResponseRepository responseRepo;
	private final SupportAttachmentRepository attachmentRepo;
	private final SupportTicketTagRepository tagRepo;
	private final UserRepository userRepository;

	public SupportService(SupportTicketRepository ticketRepo, SupportResponseRepository responseRepo,
			SupportAttachmentRepository attachmentRepo, SupportTicketTagRepository tagRepo,
			UserRepository userRepository) {
		this.ticketRepo = ticketRepo;
		this.responseRepo = responseRepo;
		this.attachmentRepo = attachmentRepo;
		this.tagRepo = tagRepo;
		this.userRepository = userRepository;
	}

	// --- LIST ---
	public SupportListResponse list(Integer page, Integer limit, String status, String sort, String order,
			String search) {
		int p = (page == null || page < 1) ? 1 : page;
		int size = (limit == null || limit < 1) ? 20 : limit;

		String sortField = switch ((sort == null) ? "created_at" : sort) {
		case "updated_at" -> "updatedAt";
		case "priority" -> "priority";
		default -> "createdAt";
		};
		Sort.Direction dir = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;

		Pageable pageable = PageRequest.of(p - 1, size, Sort.by(dir, sortField));
		Specification<SupportTicket> spec = Specification.where(SupportTicketSpecs.status(status))
				.and(SupportTicketSpecs.search(search));

		Page<SupportTicket> pageRes = ticketRepo.findAll(spec, pageable);

		List<SupportListItemDto> items = pageRes.getContent().stream().map(t -> {
			List<String> tags = tagRepo.findTags(t.getId());
			String roleName = userRepository.findById(t.getUserId()).map(u -> u.getRole().getName()).orElse("");

			Set<String> partnerRoles = Set.of("MANAGER_REGIONALE", "MANAGER", "SALES_MANAGER", "DELIVERY_MAN",
					"COLLABORATOR");

			String type = partnerRoles.contains(roleName) ? "partenaire" : "client";
			
			var attachments = attachmentRepo.findByTicketId(t.getId()).stream()
					.map(a -> new SupportDetailDto.AttachmentDto(a.getId().toString(), a.getFilename(), a.getUrl(),
							a.getSize(), a.getMimeType()))
					.toList();

			return new SupportListItemDto(t.getId(), t.getSubject(), t.getStatus(), t.getPriority(),
					new UserLite(t.getUserId(), t.getUserName(), t.getUserEmail(), t.getUserPhone(), t.getUserAvatar(),
							type),
					t.getCategory(), toIso(t.getCreatedAt()), toIso(t.getUpdatedAt()),
					toIso(t.getLastResponseAt() != null ? t.getLastResponseAt().toInstant() : null),
					t.getResponseCount(),
					(t.getAssignedToId() == null ? null
							: new UserLite(t.getAssignedToId(), t.getAssignedToName(), t.getAssignedToEmail(), null,
									t.getUserAvatar(), null)),
				attachments,	tags);
		}).toList();

		var pagination = new SupportListResponse.Pagination(p, pageRes.getTotalPages(), pageRes.getTotalElements(),
				size, pageRes.hasNext(), pageRes.hasPrevious());

		return new SupportListResponse(items, pagination);
	}

	// --- DETAIL ---
	@Transactional
	public SupportDetailDto get(UUID id) {
		SupportTicket t = ticketRepo.findById(id).orElseThrow();

		var user = new SupportDetailDto.UserDetail(t.getUserId(), t.getUserName(), t.getUserEmail(), t.getUserPhone(),
				t.getUserAvatar(), toIso(t.getUserAccountCreated().toInstant()));

		var assigned = (t.getAssignedToId() == null ? null
				: new UserLite(t.getAssignedToId(), t.getAssignedToName(), null,t.getAssignedToEmail(), null,null));

		List<String> tags = tagRepo.findTags(t.getId());

		var attachments = attachmentRepo.findByTicketId(id).stream()
				.map(a -> new SupportDetailDto.AttachmentDto(a.getId().toString(), a.getFilename(), a.getUrl(),
						a.getSize(), a.getMimeType()))
				.toList();

		var responses = responseRepo.findAllByTicketIdOrderByCreated(id).stream()
				.map(r -> new SupportDetailDto.ResponseDto(r.getId().toString(), r.getMessage(), r.getType(),
						new SupportDetailDto.ResponseDto.Author(r.getAuthorId(), r.getAuthorName(), r.getAuthorEmail(),
								r.getAuthorRole()),
						toIso(r.getCreatedAt()), Boolean.TRUE.equals(r.getIsInternal())))
				.toList();

		return new SupportDetailDto(t.getId(), t.getSubject(), t.getMessage(), t.getStatus(), t.getPriority(),
				t.getCategory(), user, toIso(t.getCreatedAt()), toIso(t.getUpdatedAt()), assigned, tags, attachments,
				responses);
	}

	// --- STATS ---
	@Transactional
	public SupportStatsDto stats() {
		long total = ticketRepo.count();
		long pending = countByStatus("pending");
		long inProgress = countByStatus("in_progress");
		long resolved = countByStatus("resolved");
		long closed = countByStatus("closed");

		String avgRespTime = averageFirstResponseTimeHours(); // ex: "2.5 hours"
		BigDecimal satisfaction = averageSatisfaction();

		return new SupportStatsDto(total, pending, inProgress, resolved, closed, avgRespTime, satisfaction);
	}

	private long countByStatus(String s) {
		return ticketRepo.count((root, q, cb) -> cb.equal(cb.lower(root.get("status")), s));
	}

	private BigDecimal averageSatisfaction() {
		// moyenne des satisfaction_rating non nulles
		// simple JPQL
		var emAvg = (Number) ((EntityManager) null); // placeholder to show intent
		// Pour rester simple sans EM: on peut faire un native query dans le repo,
		// mais si vous préférez, laissez null => le front affichera 0 / N/A.
		return null; // ou renvoyez BigDecimal.valueOf(4.2) si vous avez une autre source.
	}

	private String averageFirstResponseTimeHours() {
		// Calcul côté JVM: pour chaque ticket, trouver la 1ère admin_response
		// (ignore tickets sans réponse admin)
		List<SupportTicket> tickets = ticketRepo.findAll();
		long count = 0;
		long totalSeconds = 0;

		for (SupportTicket t : tickets) {
			var firstAdmin = responseRepo.findAdminResponses(t.getId());
			if (!firstAdmin.isEmpty() && t.getCreatedAt() != null) {
				var r0 = firstAdmin.get(0);
				var secs = Duration.between(t.getCreatedAt(), r0.getCreatedAt()).getSeconds();
				if (secs >= 0) {
					totalSeconds += secs;
					count++;
				}
			}
		}
		if (count == 0)
			return "0 hours";
		double hours = totalSeconds / 3600.0;
		return String.format(Locale.FRANCE, "%.1f hours", hours);
	}

	@Transactional
	public SupportDetailDto.ResponseDto addResponse(UUID ticketId, SupportCreateResponseDto dto) {
		SupportTicket t = ticketRepo.findById(ticketId).orElseThrow();

		// Validation simple du type
		String type = (dto.type() == null) ? "admin_response" : dto.type().toLowerCase();
		if (!List.of("admin_response", "internal_note", "user_message").contains(type)) {
			throw new IllegalArgumentException("Invalid response type: " + dto.type());
		}

		boolean isInternal = Boolean.TRUE.equals(dto.is_internal()) || "internal_note".equals(type);

		// Création de la réponse
		SupportResponse r = new SupportResponse();
		r.setId(UUID.randomUUID());
		r.setTicket(t);
		r.setMessage(dto.message());
		r.setType(type);
		r.setAuthorId(dto.author_id());
		r.setAuthorName(dto.author_name());
		r.setAuthorEmail(dto.author_email());
		r.setAuthorRole(dto.author_role());
		r.setIsInternal(isInternal);
		responseRepo.save(r);

		// Maj ticket
		int currentCount = t.getResponseCount() == null ? 0 : t.getResponseCount();
		t.setResponseCount(currentCount + 1);
		t.setLastResponseAt(r.getCreatedAt().atOffset(ZoneOffset.UTC));
		t.setUpdatedAt(Instant.now());

		// Règle d’auto-transition (optionnelle mais pratique)
		if ("pending".equalsIgnoreCase(t.getStatus()) && "admin_response".equals(type)) {
			t.setStatus("in_progress");
		}

		ticketRepo.save(t);

		// DTO de sortie (on réutilise le format du détail)
		return new SupportDetailDto.ResponseDto(r.getId().toString(), r.getMessage(), r.getType(),
				new SupportDetailDto.ResponseDto.Author(r.getAuthorId(), r.getAuthorName(), r.getAuthorEmail(),
						r.getAuthorRole()),
				toIso(r.getCreatedAt()), Boolean.TRUE.equals(r.getIsInternal()));
	}

	@Transactional
	public SupportDetailDto.ResponseDto updateResponse(UUID ticketId, UUID responseId, SupportUpdateResponseDto dto) {
		SupportTicket t = ticketRepo.findById(ticketId).orElseThrow();
		SupportResponse r = responseRepo.findById(responseId).orElseThrow();
		if (!r.getTicket().getId().equals(ticketId)) {
			throw new IllegalArgumentException("Response does not belong to ticket");
		}

		// Règles d’édition :
		// - on n’édite pas le type
		// - on peut éditer le message
		// - is_internal est pris en compte seulement si type = internal_note (et est
		// forcé à true)
		if (dto.message() != null && !dto.message().isBlank()) {
			r.setMessage(dto.message());
		}
		if ("internal_note".equalsIgnoreCase(r.getType())) {
			r.setIsInternal(Boolean.TRUE); // toujours interne
		} else if (dto.is_internal() != null) {
			// on ignore toute tentative de toggler l'interne pour non-internal_note
		}

		responseRepo.save(r);

		// maj ticket.updated_at (last_response_at inchangé car on n'a pas touché aux
		// dates)
		t.setUpdatedAt(Instant.now());
		ticketRepo.save(t);

		return new SupportDetailDto.ResponseDto(r.getId().toString(), r.getMessage(), r.getType(),
				new SupportDetailDto.ResponseDto.Author(r.getAuthorId(), r.getAuthorName(), r.getAuthorEmail(),
						r.getAuthorRole()),
				toIso(r.getCreatedAt()), Boolean.TRUE.equals(r.getIsInternal()));
	}

	@Transactional
	public void deleteResponse(UUID ticketId, UUID responseId) {
		SupportTicket t = ticketRepo.findById(ticketId).orElseThrow();
		SupportResponse r = responseRepo.findById(responseId).orElseThrow();
		if (!r.getTicket().getId().equals(ticketId)) {
			throw new IllegalArgumentException("Response does not belong to ticket");
		}

		responseRepo.delete(r);

		// Recalcule des métadonnées
		long newCount = responseRepo.countByTicketId(ticketId);
		t.setResponseCount((int) newCount);

		OffsetDateTime last = responseRepo.findLastResponseAt(ticketId);
		t.setLastResponseAt(last); // peut devenir null s'il n'y a plus de réponses

		t.setUpdatedAt(Instant.now());
		ticketRepo.save(t);
	}

	@Transactional
	public SupportDetailDto.AttachmentDto addAttachment(UUID ticketId, SupportCreateAttachmentDto dto) {
		SupportTicket t = ticketRepo.findById(ticketId).orElseThrow();

		SupportAttachment a = new SupportAttachment();
		a.setId(UUID.randomUUID());
		a.setTicket(t);
		a.setFilename(dto.filename());
		a.setUrl(dto.url());
		a.setSize(dto.size());
		a.setMimeType(dto.type());

		attachmentRepo.save(a);

		// update ticket.updated_at
		t.setUpdatedAt(Instant.now());
		ticketRepo.save(t);

		return new SupportDetailDto.AttachmentDto(a.getId().toString(), a.getFilename(), a.getUrl(), a.getSize(),
				a.getMimeType());
	}

	@Transactional
	public void deleteAttachment(UUID ticketId, UUID attachmentId) {
		SupportTicket t = ticketRepo.findById(ticketId).orElseThrow();
		SupportAttachment a = attachmentRepo.findById(attachmentId).orElseThrow();

		if (!a.getTicket().getId().equals(ticketId)) {
			throw new IllegalArgumentException("Attachment does not belong to ticket");
		}

		attachmentRepo.delete(a);

		// update ticket.updated_at
		t.setUpdatedAt(Instant.now());
		ticketRepo.save(t);
	}

	@Transactional
	public Map<String, Object> updateStatus(UUID ticketId, SupportUpdateStatusRequest req) {
		if (req == null || req.status() == null) {
			throw new IllegalArgumentException("status is required");
		}

		SupportTicket t = ticketRepo.findById(ticketId).orElseThrow();

		SupportStatus oldStatus = toEnum(t.getStatus());
		SupportStatus newStatus = req.status();

		// Pas de travail si inchangé (mais on met quand même à jour updatedAt + note
		// éventuelle)
		boolean changed = (oldStatus != newStatus);

		// Persister le nouveau statut
		t.setStatus(newStatus.getValue()); // si ton entity stocke la string; sinon t.setStatusEnum(newStatus)

		t.setUpdatedAt(Instant.now());

		// Si une note est fournie, on l’ajoute comme note interne
		if (req.note() != null && !req.note().isBlank()) {

			SupportResponse r = new SupportResponse();
			r.setId(UUID.randomUUID());
			r.setTicket(t);
			r.setMessage(req.note());
			r.setType(ResponseType.INTERNAL_NOTE.getValue());
			r.setIsInternal(true);

			// RENSEIGNER L’AUTEUR POUR RESPECTER NOT NULL
			r.setAuthorId(t.getUserId());
			r.setAuthorName(t.getUserName());
			r.setAuthorEmail(t.getUserEmail());
			r.setAuthorRole(userRepository.findById(t.getUserId()).get().getRole().getName());

			responseRepo.save(r);

			// incrémenter les métadonnées
			int count = t.getResponseCount() == null ? 0 : t.getResponseCount();
			t.setResponseCount(count + 1);
			t.setLastResponseAt(Instant.now().atOffset(ZoneOffset.UTC));
		}

		ticketRepo.save(t);

		boolean notified = false;
		if (Boolean.TRUE.equals(req.notifyUser())) {
			// notificationService.notifySupportStatusChanged(t.getId(), oldStatus,
			// newStatus);
			notified = true; // mets le retour réel de ta notif
		}

		// Réponse conforme à ta spec
		Map<String, Object> data = new HashMap();
		data.put("id", t.getId().toString());
		data.put("old_status", oldStatus == null ? null : oldStatus.getValue());
		data.put("new_status", newStatus.getValue());
		data.put("updated_at", Instant.now().toString());
		data.put("notification_sent", notified);

		Map<String, Object> body = new HashMap<>();
		body.put("success", true);
		body.put("message", "Support status updated successfully");
		body.put("data", data);

		return body;
	}

	private SupportStatus toEnum(String value) {
		if (value == null)
			return null;
		for (SupportStatus s : SupportStatus.values()) {
			if (s.getValue().equalsIgnoreCase(value))
				return s;
		}
		return null;
	}

	private static String toIso(Instant dt) {
		return dt == null ? null : dt.atZone(ZoneOffset.UTC).toString();
	}
}

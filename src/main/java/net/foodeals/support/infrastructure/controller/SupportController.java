package net.foodeals.support.infrastructure.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.foodeals.support.application.dtos.SupportCreateAttachmentDto;
import net.foodeals.support.application.dtos.SupportCreateResponseDto;
import net.foodeals.support.application.dtos.SupportDetailDto;
import net.foodeals.support.application.dtos.SupportListResponse;
import net.foodeals.support.application.dtos.SupportStatsDto;
import net.foodeals.support.application.dtos.SupportUpdateResponseDto;
import net.foodeals.support.application.dtos.SupportUpdateStatusRequest;
import net.foodeals.support.application.services.SupportService;

@RestController
@RequestMapping("v1/support")
@AllArgsConstructor
public class SupportController {
    private final SupportService service;
  

    @GetMapping("/statistics")
    public SupportStatsDto statistics() {
        return service.stats();
    }

    @GetMapping
    public SupportListResponse list(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "20") Integer limit,
        @RequestParam(defaultValue = "all") String status,
        @RequestParam(defaultValue = "created_at") String sort,
        @RequestParam(defaultValue = "desc") String order,
        @RequestParam(required = false) String search
    ) {
        return service.list(page, limit, status, sort, order, search);
    }

    @GetMapping("/{id}")
    public SupportDetailDto get(@PathVariable UUID id) {
        return service.get(id);
    }
    
    @PostMapping("/{id}/responses")
    public SupportDetailDto.ResponseDto addResponse(
        @PathVariable UUID id,
        @RequestBody @Valid SupportCreateResponseDto dto
    ) {
        return service.addResponse(id, dto);
    }
    
    @PutMapping("/{ticketId}/responses/{responseId}")
    public SupportDetailDto.ResponseDto updateResponse(
        @PathVariable UUID ticketId,
        @PathVariable UUID responseId,
        @RequestBody @Valid SupportUpdateResponseDto dto
    ) {
        return service.updateResponse(ticketId, responseId, dto);
    }
    
    @PatchMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable UUID id,
                                            @RequestBody SupportUpdateStatusRequest req) {
        return service.updateStatus(id, req);
    }

    @DeleteMapping("/{ticketId}/responses/{responseId}")
    public void deleteResponse(
        @PathVariable UUID ticketId,
        @PathVariable UUID responseId
    ) {
        service.deleteResponse(ticketId, responseId);
    }
    
    @PostMapping("/{id}/attachments")
   
    public SupportDetailDto.AttachmentDto addAttachment(
        @PathVariable UUID id,
        @RequestBody @Valid SupportCreateAttachmentDto dto
    ) {
        return service.addAttachment(id, dto);
    }

    @DeleteMapping("/{ticketId}/attachments/{attachmentId}")
    public void deleteAttachment(
        @PathVariable UUID ticketId,
        @PathVariable UUID attachmentId
    ) {
        service.deleteAttachment(ticketId, attachmentId);
    }
}

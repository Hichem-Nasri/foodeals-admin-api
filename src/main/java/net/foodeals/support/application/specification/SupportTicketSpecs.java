package net.foodeals.support.application.specification;

import org.springframework.data.jpa.domain.Specification;

import net.foodeals.support.domain.entities.SupportTicket;

public final class SupportTicketSpecs {
    private SupportTicketSpecs() {}

    public static Specification<SupportTicket> status(String status) {
        return (root, q, cb) -> (status == null || status.equalsIgnoreCase("all"))
            ? cb.conjunction()
            : cb.equal(cb.lower(root.get("status")), status.toLowerCase());
    }

    public static Specification<SupportTicket> search(String s) {
        return (root, q, cb) -> {
            if (s == null || s.isBlank()) return cb.conjunction();
            String like = "%" + s.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("subject")), like),
                cb.like(cb.lower(root.get("message")), like)
            );
        };
    }
}

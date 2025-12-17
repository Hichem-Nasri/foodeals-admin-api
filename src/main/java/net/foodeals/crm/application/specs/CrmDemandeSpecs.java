package net.foodeals.crm.application.specs;


import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import net.foodeals.crm.domain.entities.CrmDemande;

import java.time.OffsetDateTime;

public final class CrmDemandeSpecs {
    private CrmDemandeSpecs(){}

    public static Specification<CrmDemande> typeEq(String type) {
        return (root, q, cb) -> (type==null || type.isBlank())
            ? cb.conjunction()
            : cb.equal(cb.lower(root.get("type").as(String.class)), type.toLowerCase());
    }
    public static Specification<CrmDemande> countryEq(String v){
        return (r,q,cb) -> (v==null || v.isBlank()) ? cb.conjunction() : cb.equal(cb.lower(r.get("country")), v.toLowerCase());
    }
    public static Specification<CrmDemande> cityEq(String v){
        return (r,q,cb) -> (v==null || v.isBlank()) ? cb.conjunction() : cb.equal(cb.lower(r.get("city")), v.toLowerCase());
    }
    public static Specification<CrmDemande> statusEq(String v){
        return (r,q,cb) -> (v==null || v.isBlank()) ? cb.conjunction() : cb.equal(cb.lower(r.get("status").as(String.class)), v.toLowerCase());
    }
    public static Specification<CrmDemande> activityHas(String v){
        return (r,q,cb) -> {
            if (v==null || v.isBlank()) return cb.conjunction();
            Join<CrmDemande, String> act = r.joinSet("activity", JoinType.LEFT);
            return cb.equal(cb.lower(act), v.toLowerCase());
        };
    }
    public static Specification<CrmDemande> createdBetween(OffsetDateTime from, OffsetDateTime to){
        return (r,q,cb) -> {
            Path<OffsetDateTime> p = r.get("createdAt");
            if (from==null && to==null) return cb.conjunction();
            if (from!=null && to!=null) return cb.between(p, from, to);
            return from!=null ? cb.greaterThanOrEqualTo(p, from)
                              : cb.lessThanOrEqualTo(p, to);
        };
    }
    public static Specification<CrmDemande> search(String s){
        return (r,q,cb) -> {
            if (s==null || s.isBlank()) return cb.conjunction();
            String like = "%" + s.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(r.get("companyName")), like),
                cb.like(cb.lower(r.get("address")), like),
                cb.like(cb.lower(r.get("email")), like),
                cb.like(cb.lower(r.get("phone")), like),
                cb.like(cb.lower(r.get("notes")), like)
            );
        };
    }
}

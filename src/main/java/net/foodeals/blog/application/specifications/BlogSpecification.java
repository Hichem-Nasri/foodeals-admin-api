package net.foodeals.blog.application.specifications;

import jakarta.persistence.criteria.Predicate;
import net.foodeals.blog.domain.entity.Blog;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlogSpecification {

    public static Specification<Blog> filter(
            String title,
            UUID categoryId,
            Boolean published
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (title != null) {
                predicates.add(cb.like(
                        cb.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                ));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(
                        root.get("category").get("id"),
                        categoryId
                ));
            }

            if (published != null) {
                predicates.add(cb.equal(root.get("published"), published));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


package net.foodeals.blog.domain.repository;

import net.foodeals.blog.domain.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, UUID> {

    Optional<BlogCategory> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}

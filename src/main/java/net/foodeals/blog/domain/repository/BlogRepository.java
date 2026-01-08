package net.foodeals.blog.domain.repository;

import net.foodeals.blog.domain.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID>, JpaSpecificationExecutor<Blog> {

    Page<Blog> findByDeletedAtIsNull(Pageable pageable);

    Page<Blog> findByDeletedAtIsNotNull(Pageable pageable);
}

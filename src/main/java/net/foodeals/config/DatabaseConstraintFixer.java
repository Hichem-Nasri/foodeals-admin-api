package net.foodeals.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseConstraintFixer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConstraintFixer.class);

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void ensureOrganizationEntityTypeConstraint() {
        try {
            jdbcTemplate.execute(
                    "ALTER TABLE organization_entities DROP CONSTRAINT IF EXISTS organization_entities_type_check"
            );
            jdbcTemplate.execute(
                    "ALTER TABLE organization_entities " +
                            "ADD CONSTRAINT organization_entities_type_check " +
                            "CHECK (type >= 0 AND type <= 5)"
            );
            logger.info("organization_entities_type_check updated to allow enum ordinals 0..5");
        } catch (Exception e) {
            logger.warn("Failed to update organization_entities_type_check: {}", e.getMessage());
        }
    }
}

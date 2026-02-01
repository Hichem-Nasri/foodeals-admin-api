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

        ensurePaymentMethodDiscriminator();
    }

    private void ensurePaymentMethodDiscriminator() {
        try {
            Integer dtypeExists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.columns " +
                            "WHERE table_schema = 'public' AND table_name = 'payment_method' AND column_name = 'dtype'",
                    Integer.class
            );
            if (dtypeExists == null || dtypeExists == 0) {
                jdbcTemplate.execute("ALTER TABLE payment_method ADD COLUMN dtype VARCHAR(31)");

                Integer methodTypeExists = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM information_schema.columns " +
                                "WHERE table_schema = 'public' AND table_name = 'payment_method' AND column_name = 'method_type'",
                        Integer.class
                );
                if (methodTypeExists != null && methodTypeExists > 0) {
                    jdbcTemplate.execute("UPDATE payment_method SET dtype = method_type WHERE dtype IS NULL");
                }

                jdbcTemplate.execute(
                        "UPDATE payment_method SET dtype = CASE " +
                                "WHEN card_number IS NOT NULL OR payment_id IS NOT NULL OR card_holder_name IS NOT NULL THEN 'CARD' " +
                                "WHEN cheque_number IS NOT NULL OR cheque_document IS NOT NULL OR issuer IS NOT NULL OR bank IS NOT NULL THEN 'CHEQUE' " +
                                "WHEN document_path IS NOT NULL OR payed_at IS NOT NULL THEN 'BANK_TRANSFER' " +
                                "WHEN recuperation_date IS NOT NULL THEN 'CASH' " +
                                "ELSE 'CASH' END " +
                                "WHERE dtype IS NULL"
                );
                logger.info("payment_method.dtype column added and backfilled");
            }
        } catch (Exception e) {
            logger.warn("Failed to ensure payment_method.dtype column: {}", e.getMessage());
        }
    }
}

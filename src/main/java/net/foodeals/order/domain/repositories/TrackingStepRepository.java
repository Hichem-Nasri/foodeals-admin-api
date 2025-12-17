package net.foodeals.order.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.order.domain.entities.TrackingStep;

public interface TrackingStepRepository extends BaseRepository<TrackingStep, Long> {
    List<TrackingStep> findByOrderIdOrderByTimestampAsc(UUID orderId);
}

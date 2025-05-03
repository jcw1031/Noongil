package com.woopaca.noongil.domain.safety;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface SafetyRepository extends JpaRepository<Safety, Long> {

    Collection<Safety> findByUpdatedAtIsGreaterThanEqual(LocalDateTime updatedAt);

    Optional<Safety> findByUpdatedAtIsGreaterThanEqualAndUserId(LocalDateTime updatedAt, Long userId);
}

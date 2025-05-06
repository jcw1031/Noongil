package com.woopaca.noongil.domain.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Optional<Activity> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);

    Collection<Activity> findByUserIdAndActivityDateIsGreaterThanEqual(Long userId, LocalDate startDate);

    @Query("""
            SELECT DISTINCT a.userId
            FROM Activity a
            WHERE a.activityDate >= :startDate
            """)
    Collection<Long> findRecentActivitiesUserIds(@Param("startDate") LocalDate startDate);

    Collection<Activity> findByActivityDate(LocalDate activityDate);
}

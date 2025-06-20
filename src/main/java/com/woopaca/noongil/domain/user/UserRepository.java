package com.woopaca.noongil.domain.user;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    void acquireExclusiveLock(Long id);

    Optional<User> findByContact(String contact);

    Optional<User> findByIdentifier(String identifier);

    Collection<User> findByName(String name);

    @Query("""
            SELECT u.pushToken
            FROM User u
            WHERE u.status = :status
            """)
    Collection<String> findPushTokensByStatus(AccountStatus status);

    Collection<User> findByStatus(AccountStatus status);
}

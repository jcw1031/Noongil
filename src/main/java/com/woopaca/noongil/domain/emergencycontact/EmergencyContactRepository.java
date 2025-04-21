package com.woopaca.noongil.domain.emergencycontact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    Collection<EmergencyContact> findByUserId(Long userId);

    Collection<EmergencyContact> findByContact(String contact);
}

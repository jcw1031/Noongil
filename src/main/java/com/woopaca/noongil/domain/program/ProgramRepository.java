package com.woopaca.noongil.domain.program;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    @Query("""
            SELECT new com.woopaca.noongil.domain.program.ProgramsCountByBorough(p.borough, COUNT(p))
            FROM Program AS p
            GROUP BY p.borough
            """)
    Collection<ProgramsCountByBorough> countEachBorough();

    Collection<Program> findByBorough(String borough);
}

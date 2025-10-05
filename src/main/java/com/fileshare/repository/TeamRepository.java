package com.fileshare.repository;

import com.fileshare.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
 Optional<Team> findByTeamName(String teamName);     // ✅ CORRECT method name
    boolean existsByTeamName(String teamName);    
}
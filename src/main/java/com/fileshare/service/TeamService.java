package com.fileshare.service;

import com.fileshare.model.Team;
import com.fileshare.model.User;
import com.fileshare.repository.TeamRepository;
import com.fileshare.repository.UserRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
private PasswordEncoder passwordEncoder;


    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    public String joinTeam(String teamName, String password, String username) {
    Optional<Team> teamOpt = teamRepository.findByTeamName(teamName);
    if (teamOpt.isEmpty()) {
        return "❌ Team does not exist";
    }

    Team team = teamOpt.get();
     // 🔍 Debug logs to check what's going wrong
    System.out.println("Raw password: " + password);
    System.out.println("Encoded password in DB: " + team.getPassword());

    if (!passwordEncoder.matches(password, team.getPassword())) {

        return "❌ Incorrect password for team";
    }

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

            // If already admin 
            if ("ADMIN".equals(user.getRole())) {
                return "❌ Admins are not allowed to join teams using this method";
            }

    // ✅ Check if already in team
    if (user.getTeam() != null && user.getTeam().getId().equals(team.getId())) {
        return "✅ You are already part of this team";
    }

    // For regular users or users not in any team
    user.setTeam(team);
    // Only set role to USER if they're not already an ADMIN
    if (!"ADMIN".equals(user.getRole())) {
        user.setRole("USER");
    }
    
    userRepository.save(user);
    return "✅ Joined team successfully";


}

public String createTeam(String teamName, String password, String username) {
    if (teamRepository.existsByTeamName(teamName)) {
        return "❌ Team already exists";
    }

    Team team = new Team();
    team.setTeamName(teamName);
    team.setPassword(passwordEncoder.encode(password));
    teamRepository.save(team);

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setTeam(team);
    user.setRole("ADMIN");
    userRepository.save(user);

    return "✅ Team created successfully";
}
public String adminJoinTeam(String teamName, String password, String username) {
    Optional<Team> teamOpt = teamRepository.findByTeamName(teamName);
    if (teamOpt.isEmpty()) return "❌ Team not found";

    Team team = teamOpt.get();

if (!passwordEncoder.matches(password, team.getPassword())) {
    return "❌ Incorrect team password";
}



    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Allow only if the team they're joining is the one they created
    if (!"ADMIN".equals(user.getRole())) {
        return "❌ Only Admins can use Admin Join";
    }

    if (user.getTeam() == null || !user.getTeam().getId().equals(team.getId())) {
        return "❌ You are admin of another team. Cannot join this team.";
    }

    return "✅ Welcome back, Admin!";
}


}
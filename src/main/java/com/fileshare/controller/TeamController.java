package com.fileshare.controller;


import com.fileshare.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.security.Principal;


import java.util.Map;
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired private TeamService teamService;

    @PostMapping("join")
    public ResponseEntity<?> joinTeam(@RequestBody Map<String, String> payload, Principal principal) {
        String teamName = payload.get("teamName");
        String password = payload.get("password");
        String username = principal.getName();

        String result = teamService.joinTeam(teamName, password, username);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestBody Map<String, String> payload, Principal principal) {
        String teamName = payload.get("teamName");
        String password = payload.get("password");
        String username = principal.getName();

        String result = teamService.createTeam(teamName, password, username);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/admin/join")
public ResponseEntity<?> adminJoinTeam(@RequestBody Map<String, String> payload, Principal principal) {
    String teamName = payload.get("teamName");
    String password = payload.get("password");
    String username = principal.getName();

    String result = teamService.adminJoinTeam(teamName, password, username);
    return ResponseEntity.ok(result);
}

}
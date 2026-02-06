package com.NirajCS.MoneyManager.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.NirajCS.MoneyManager.dto.AuthDTO;
import com.NirajCS.MoneyManager.dto.ProfileDTO;
import com.NirajCS.MoneyManager.services.ProfileServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class ProfileController {
    private final ProfileServices profileServices;
    
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registeredProfile = profileServices.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }
    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean isActivated = profileServices.activateProfile(token);
        if (isActivated) {
            return ResponseEntity.ok("Profile activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid activation token.");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> loginProfile(@RequestBody AuthDTO authDTO) {
        try {
        if(!profileServices.isAccountActive(authDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Account is not activated. Please check your email for activation link."));    
            }
        Map<String, Object> response = profileServices.authenticateAndGenerateToken(authDTO);
        return ResponseEntity.ok(response);
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Login failed"));
        }

    }
    @GetMapping("/test") 
        public String test(){
        return "Test Successful";
        }


}

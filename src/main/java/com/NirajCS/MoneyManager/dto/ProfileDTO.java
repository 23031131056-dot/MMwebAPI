package com.NirajCS.MoneyManager.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class ProfileDTO {

    private Long id;
    private String fullname;
    private String email;
    private String password;
    private String ProflieImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String ActivationToken;
}

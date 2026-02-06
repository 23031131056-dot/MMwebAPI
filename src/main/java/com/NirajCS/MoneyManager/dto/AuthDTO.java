package com.NirajCS.MoneyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthDTO {

    private String email;
    private String password;
    private String token;
}

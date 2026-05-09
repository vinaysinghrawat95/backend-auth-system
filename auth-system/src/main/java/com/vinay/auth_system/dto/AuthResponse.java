package com.vinay.auth_system.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String jwtToken;
    private String refreshToken;
    private String type;

    public AuthResponse(String jwtToken, String refreshToken){
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.type = "success";
    }

}

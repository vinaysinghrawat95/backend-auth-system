package com.vinay.auth_system.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String jwtToken;
    private String type;

    public AuthResponse(String jwtToken){
        this.jwtToken = jwtToken;
        this.type = "success";
    }

}

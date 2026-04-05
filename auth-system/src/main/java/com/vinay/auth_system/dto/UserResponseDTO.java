package com.vinay.auth_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {

    private long id;
    private String username;
    private String email;

}

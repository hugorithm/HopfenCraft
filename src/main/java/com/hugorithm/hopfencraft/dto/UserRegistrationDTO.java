package com.hugorithm.hopfencraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String email;

    public UserRegistrationDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}


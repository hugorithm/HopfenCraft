package com.hugorithm.hopfencraft.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String username;
    private String email;
    private String jwt;
    private List<String> roles;
}

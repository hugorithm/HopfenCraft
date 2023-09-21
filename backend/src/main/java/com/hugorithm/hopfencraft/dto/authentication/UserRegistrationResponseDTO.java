package com.hugorithm.hopfencraft.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponseDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}

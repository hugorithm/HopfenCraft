package com.hugorithm.hopfencraft.dto.authentication;

import com.hugorithm.hopfencraft.validators.Password;
import com.hugorithm.hopfencraft.validators.Username;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    @Username
    private String username;
    @Password
    private String password;
    @Email
    private String email;
}


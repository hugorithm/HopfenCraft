package com.hugorithm.hopfencraft.dto.authentication;

import com.hugorithm.hopfencraft.validators.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {
    @Password
    private String oldPassword;
    @Password
    private String newPassword;
    @Password
    private String newPasswordConfirmation;
}

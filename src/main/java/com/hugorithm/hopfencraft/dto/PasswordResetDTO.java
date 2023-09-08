package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.validators.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {
    private ApplicationUser user;
    @Password
    private String oldPassword;
    @Password
    private String newPassword;
    @Password
    private String newPasswordConfirmation;
}

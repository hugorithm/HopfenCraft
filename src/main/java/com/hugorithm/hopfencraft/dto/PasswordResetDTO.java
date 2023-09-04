package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {
    private ApplicationUser user;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirmation;
}

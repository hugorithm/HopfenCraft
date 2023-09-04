package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.ApplicationUser;

public class PasswordResetDTO {
    private ApplicationUser user;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirmation;

    public PasswordResetDTO() {
    }

    public PasswordResetDTO(ApplicationUser user, String oldPassword, String newPassword, String newPasswordConfirmation) {
        this.user = user;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}

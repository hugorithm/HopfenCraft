package com.hugorithm.hopfencraft.dto.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hugorithm.hopfencraft.validators.AgeConstraint;
import com.hugorithm.hopfencraft.validators.Password;
import com.hugorithm.hopfencraft.validators.Phone;
import com.hugorithm.hopfencraft.validators.Username;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Past
    @AgeConstraint
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Phone
    private String phoneNumber;
}


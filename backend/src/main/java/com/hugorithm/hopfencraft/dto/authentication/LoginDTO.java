package com.hugorithm.hopfencraft.dto.authentication;

import com.hugorithm.hopfencraft.validators.Password;
import com.hugorithm.hopfencraft.validators.Username;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @Username
    private String username;
    @Password
    private String password;
}

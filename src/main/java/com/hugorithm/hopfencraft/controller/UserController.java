package com.hugorithm.hopfencraft.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
//See CrossOrigin after
public class UserController {
    @PostMapping("/change-password")
    public String changePassword() {
        return "User access level";
    }
}

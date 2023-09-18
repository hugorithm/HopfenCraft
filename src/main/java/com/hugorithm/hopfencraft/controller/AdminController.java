package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.user.ApplicationUserDTO;
import com.hugorithm.hopfencraft.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<ApplicationUserDTO>> getUsers(){
        return adminService.getUsers();
    }

    @GetMapping("/user/{userId}")
    public  ResponseEntity<ApplicationUserDTO> getUserById(@PathVariable Long userId) {
        return adminService.getUserById(userId);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApplicationUserDTO> getUserByUsername(@PathVariable String username) {
        return adminService.getUserByUsername(username);
    }
}

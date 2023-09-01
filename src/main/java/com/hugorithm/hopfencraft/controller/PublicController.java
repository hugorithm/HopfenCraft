package com.hugorithm.hopfencraft.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class PublicController {
    @GetMapping("/")
    public String helloPublicController() {
        return "public access level";
    }
}

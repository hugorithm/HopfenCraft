package com.hugorithm.hopfencraft.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marketplace")
@CrossOrigin("*")
public class MarketplaceController {
    @GetMapping("/products")
    public String helloMarketplaceController() {
        return "products access level";
    }
}

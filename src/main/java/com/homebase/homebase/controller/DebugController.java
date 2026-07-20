package com.homebase.homebase.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @GetMapping("/whoami")
    public String whoAmI(Authentication authentication) {
        if (authentication == null) {
            return "No authentication in context";
        }
        return "Authenticated as: " + authentication.getName();
    }
}
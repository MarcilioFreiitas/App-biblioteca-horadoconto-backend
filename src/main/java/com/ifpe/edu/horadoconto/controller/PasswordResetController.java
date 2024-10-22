package com.ifpe.edu.horadoconto.controller;

import com.ifpe.edu.horadoconto.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordResetController {
    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping(value = "/reset", consumes = "application/x-www-form-urlencoded")
    public String resetPassword(@RequestParam("email") String email) {
        return passwordResetService.initiatePasswordReset(email);
    }

    @PostMapping("/save")
    public String savePassword(@RequestBody PasswordResetRequest request) {
        return passwordResetService.saveNewPassword(request.getToken(), request.getPassword());
    }
}

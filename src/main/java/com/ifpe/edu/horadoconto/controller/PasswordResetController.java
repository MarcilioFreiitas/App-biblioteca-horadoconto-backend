package com.ifpe.edu.horadoconto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ifpe.edu.horadoconto.model.PasswordResetRequest;
import com.ifpe.edu.horadoconto.service.PasswordResetService;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping(value = "/reset", consumes = "application/x-www-form-urlencoded")
    public String resetPassword(@RequestParam("email") String email) {
        return passwordResetService.initiatePasswordReset(email);
    }

    @PostMapping(value = "/validate", consumes = "application/x-www-form-urlencoded")
    public String validateCodigo(@RequestParam("email") String email, @RequestParam("codigo") String codigo) {
        boolean isValid = passwordResetService.validateCodigo(email, codigo);
        return isValid ? "Código válido" : "Código inválido ou expirado";
    }

    @PostMapping("/save")
    public String savePassword(@RequestBody PasswordResetRequest request) {
        return passwordResetService.saveNewPassword(request.getToken(), request.getPassword());
    }
}

package com.ifpe.edu.horadoconto.model;

public class PasswordResetRequest {
    private String email;
    private String password;
    private String token;

    // Construtor
    public PasswordResetRequest() {}

    public PasswordResetRequest(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

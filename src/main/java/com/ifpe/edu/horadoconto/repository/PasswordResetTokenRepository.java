package com.ifpe.edu.horadoconto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpe.edu.horadoconto.model.PasswordResetToken;
import com.ifpe.edu.horadoconto.model.Usuario;

import jakarta.transaction.Transactional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByTokenAndUsuario(String token, Usuario usuario);

    @Transactional
    void deleteByUsuario(Usuario usuario);
}

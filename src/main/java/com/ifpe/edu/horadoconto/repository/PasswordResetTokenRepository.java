package com.ifpe.edu.horadoconto.repository;

import com.ifpe.edu.horadoconto.model.PasswordResetToken;
import com.ifpe.edu.horadoconto.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

	void deleteByUsuario(Usuario usuario);
}

package com.ifpe.edu.horadoconto.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.ifpe.edu.horadoconto.model.PasswordResetToken;
import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.repository.PasswordResetTokenRepository;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public String initiatePasswordReset(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return "Email não encontrado";
        }

        // Excluir códigos existentes para o usuário
        tokenRepository.deleteByUsuario(usuario);

        // Gerar um código de 4 dígitos
        String codigo = String.format("%04d", new Random().nextInt(10000));

        // Definir data de expiração
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date expiryDate = new Date(calendar.getTimeInMillis());

        PasswordResetToken resetToken = new PasswordResetToken(codigo, usuario, expiryDate);
        tokenRepository.save(resetToken);

        // Enviar email com o código
        String emailContent = "<html><body><p>Seu código de redefinição de senha é: <strong>" + codigo + "</strong></p></body></html>";
        emailService.sendEmail(email, "Redefinição de Senha", emailContent);

        return "Código de redefinição enviado para o email";
    }

    public boolean validateCodigo(String email, String codigo) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return false;
        }

        PasswordResetToken token = tokenRepository.findByTokenAndUsuario(codigo, usuario);
        if (token == null || isTokenExpired(token)) {
            return false;
        }

        return true;
    }

    @Transactional
    public String saveNewPassword(String token, String newPassword) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken == null) {
            return "Token inválido";
        }
        if (isTokenExpired(passToken)) {
            return "Token expirado";
        }
        Usuario usuario = passToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);

        // Excluir o token do banco de dados após a redefinição da senha
        tokenRepository.delete(passToken);

        return "Senha redefinida com sucesso";
    }

    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().before(new Date());
    }
}

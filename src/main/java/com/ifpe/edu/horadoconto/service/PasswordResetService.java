package com.ifpe.edu.horadoconto.service;

import com.ifpe.edu.horadoconto.model.PasswordResetToken;
import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.repository.PasswordResetTokenRepository;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String initiatePasswordReset(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return "Email não encontrado";
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, usuario);
        tokenRepository.save(myToken);

        // URL do aplicativo Flutter com o token
        String resetUrl = "myapp://password-reset?token=" + token;
        String emailContent = "<p>Clique no link para redefinir sua senha: <a href=\"" + resetUrl + "\">Redefinir Senha</a></p>";

        emailService.sendEmail(email, "Redefinição de Senha", emailContent);

        return "Email enviado";
    }


    public String saveNewPassword(String token, String newPassword) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken == null) {
            return "Token inválido";
        }

        Usuario usuario = passToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(newPassword)); // Criptografar a senha
        usuarioRepository.save(usuario);

        return "Senha redefinida com sucesso";
    }
}

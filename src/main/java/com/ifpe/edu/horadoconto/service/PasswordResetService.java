package com.ifpe.edu.horadoconto.service;

import com.ifpe.edu.horadoconto.model.PasswordResetToken;
import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.repository.PasswordResetTokenRepository;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.Date;
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
        // Excluir tokens existentes para o usuário
        tokenRepository.deleteByUsuario(usuario);

       
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date expiryDate = new Date(calendar.getTimeInMillis());

        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, usuario, expiryDate);
        tokenRepository.save(myToken);

        String resetUrl = "http://10.0.0.105:8080/password-reset.html?token=" + token;
        String emailContent = "<html><body><p>Clique no link para redefinir sua senha: <a href=\"" + resetUrl + "\">Redefinir Senha</a></p></body></html>";

        emailService.sendEmail(email, "Redefinição de Senha", emailContent);
        return "Email enviado";
    }


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

        // Excluir o token do banco de dados
        tokenRepository.delete(passToken);

        return "Senha redefinida com sucesso";
    }

    
    public boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().before(new Date());
    }

}


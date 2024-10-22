package com.ifpe.edu.horadoconto.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ifpe.edu.horadoconto.model.Admin;
import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class TokenService {
    
	@Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private UsuarioRepository repository;
    
    
 
    
    public String generateToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("role", user.getRole().name())
                    .withClaim("id", user.getId())
                    .withClaim("nome", user.getNome())
                    .withClaim("sobreNome", user.getSobreNome())
                    .withClaim("cpf", user.getCpf())
                    .withClaim("email", user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    
    public String generateTokenAdmin(Admin admin){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(admin.getEmail())
                    .withClaim("role", admin.getRole().name())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }
 
    
    
    

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }
    
    public long getUserIdFromToken(String token) {
    	  // Remova o prefixo "Bearer " do token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Verifique e decodifique o token
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("auth-api")
                .build()
                .verify(token);

        // Retorne o ID do usuário como Long
        return Long.parseLong(jwt.getSubject());
    }


    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

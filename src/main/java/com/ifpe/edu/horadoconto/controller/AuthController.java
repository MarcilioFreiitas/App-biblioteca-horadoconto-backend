package com.ifpe.edu.horadoconto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.model.Admin;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;
import com.ifpe.edu.horadoconto.infra.security.TokenService;


import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
    private AuthenticationManager authenticationManager;
    
	@Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private TokenService tokenService;
	
	
    
    
    
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AuthDTO data) {
	var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());	
	var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = tokenService.generateToken((Usuario) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDTO(token));
    		
    	
	}

	
	@PostMapping("/loginadmin")
	public ResponseEntity loginAdmin(@RequestBody @Valid AuthDTO data) {
	var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());	
	var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = tokenService.generateTokenAdmin((Admin) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDTO(token));
    		
    	
	}
	
	
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
	    if(this.repository.findByemail(data.email()) != null) return ResponseEntity.badRequest().build();

	    String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
	    // Adicionando os novos campos ao construtor do Usuario
	    Usuario newUser = new Usuario(data.nome(), data.sobreNome(), data.cpf(), data.email(), encryptedPassword, data.role());

	    this.repository.save(newUser);

	    return ResponseEntity.ok().build();
	}

	
	
	
}

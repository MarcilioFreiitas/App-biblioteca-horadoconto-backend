package com.ifpe.edu.horadoconto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.repository.AdminRepository;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;

@Service("authService")
public class AuthService implements UserDetailsService {

	
	    @Autowired
	    UsuarioRepository usuarioRepository;

	    @Autowired
	    AdminRepository adminRepository;
	    
	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        UserDetails userDetails = usuarioRepository.findByemail(username);
	        
	        if (userDetails == null) {
	            userDetails = adminRepository.findByemail(username);
	        }
	        
	        if (userDetails == null) {
	            throw new UsernameNotFoundException("User not found");
	        }
	        
	        return userDetails;
	    }
	    
	    public Usuario findById(Long id) {
	        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
	        if (usuarioOpt.isPresent()) {
	            return usuarioOpt.get();
	        } else {
	            throw new UsernameNotFoundException("Usuário não encontrado com o ID: " + id);
	        }
	    }

}

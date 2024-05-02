package com.ifpe.edu.horadoconto.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
   
	@Autowired
	SecurityFilter securityFilter;
	
	  @Autowired
	  UserDetailsService userDetailsService;

	  @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	        return httpSecurity
	                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
	                .csrf(csrf -> csrf.disable())
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authorizeHttpRequests(authorize -> authorize
	                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/auth/loginadmin").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/livros").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/livros/salvar").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/livros/uploadCapa").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/livros/listar").permitAll()
	                        .requestMatchers(HttpMethod.DELETE, "/livros/apagar/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE, "/usuarios/apagar/**").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/usuarios/listar").permitAll()
	                        .requestMatchers(HttpMethod.PUT, "/livros/alterar/**").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/emprestimo/emprestar").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/emprestimo/listarEmprestimo").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/usuarios/salvar").permitAll()
	                        .requestMatchers(HttpMethod.PUT, "/usuarios/alterar/**").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/imagens/capas/**").permitAll()
	                        	                       
	                )
	                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
	                .build();
	    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
    
    
}
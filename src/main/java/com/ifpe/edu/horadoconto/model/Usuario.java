package com.ifpe.edu.horadoconto.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;



@Entity
@NoArgsConstructor
public class Usuario implements UserDetails {

	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @Column
	    @NotNull
	    private String nome;

	    @Column
	    @NotNull
	    private String sobreNome;

	    @Column
	    @NotNull
	    private String cpf;

	    @Column
	    @NotNull
	    private String email;

	    @Column
	    @NotNull
	    private String senha;
	    
	    @Column
	    @NotNull
	    private UserRoles role;
	    
	    @JsonManagedReference("usuario-emprestimo")
	    @OneToMany(mappedBy = "usuario")
	    private Set<Emprestimo> emprestimos;
	    

		
	    public Usuario() {
	    }
	    
	    public Usuario (String nome, String sobreNome, String cpf,String email, String encryptedPassword, UserRoles role){
	        this.nome = nome;
	        this.sobreNome = sobreNome;
	        this.cpf = cpf;
	    	this.email = email;
	        this.senha = encryptedPassword;
	        this.role = role;
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getSobreNome() {
			return sobreNome;
		}

		public void setSobreNome(String sobreNome) {
			this.sobreNome = sobreNome;
		}

		public String getCpf() {
			return cpf;
		}

		public void setCpf(String cpf) {
			this.cpf = cpf;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

		public UserRoles getRole() {
			return role;
		}

		public void setRole(UserRoles role) {
			this.role = role;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			if(this.role == UserRoles.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
	        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));		}
		
		
		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return senha;
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return email;
		}

		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}
	
	    
	    
	
}

package com.ifpe.edu.horadoconto.controller;

import java.util.List;

import com.ifpe.edu.horadoconto.model.UserRoles;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String sobreNome;
    private String cpf;
    private String email;
   
    private UserRoles role;
    private List<EmprestimoDTO> emprestimos;
	
    public UsuarioDTO(Long id, String nome, String sobreNome, String cpf, String email,  UserRoles role,
			List<EmprestimoDTO> emprestimos) {
		super();
		this.id = id;
		this.nome = nome;
		this.sobreNome = sobreNome;
		this.cpf = cpf;
		this.email = email;
		
		this.role = role;
		this.emprestimos = emprestimos;
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



	public UserRoles getRole() {
		return role;
	}

	public void setRole(UserRoles role) {
		this.role = role;
	}

	public List<EmprestimoDTO> getEmprestimos() {
		return emprestimos;
	}

	public void setEmprestimos(List<EmprestimoDTO> emprestimos) {
		this.emprestimos = emprestimos;
	}

   
	
    
    
}

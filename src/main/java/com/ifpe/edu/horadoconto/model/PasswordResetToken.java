package com.ifpe.edu.horadoconto.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    
    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Usuario usuario;
    private Date expiryDate;

    // Construtor padrão
    public PasswordResetToken() {}

    // Construtor que inicializa todos os campos
    public PasswordResetToken(Long id, String token, Usuario usuario, Date expiryDate) {
        this.id = id;
        this.token = token;
        this.usuario = usuario;
        this.expiryDate = expiryDate;
    }

    // Construtor que inicializa apenas token e usuario
    public PasswordResetToken(String token, Usuario usuario, Date expiryDate) {
        this.token = token;
        this.usuario = usuario;
        this.expiryDate = expiryDate;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

    // Getters e setters
}

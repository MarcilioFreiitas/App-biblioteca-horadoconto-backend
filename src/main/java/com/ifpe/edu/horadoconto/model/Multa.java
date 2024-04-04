package com.ifpe.edu.horadoconto.model;

import org.springframework.data.relational.core.mapping.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column
    @NotNull
    private String valorMulta;
    
    @Column
    @NotNull
    private String dataMulta;
    
    @Column
    @NotNull
    private String motivoMulta;
	
    
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getValorMulta() {
		return valorMulta;
	}
	public void setValorMulta(String valorMulta) {
		this.valorMulta = valorMulta;
	}
	public String getDataMulta() {
		return dataMulta;
	}
	public void setDataMulta(String dataMulta) {
		this.dataMulta = dataMulta;
	}
	public String getMotivoMulta() {
		return motivoMulta;
	}
	public void setMotivoMulta(String motivoMulta) {
		this.motivoMulta = motivoMulta;
	}

    
}
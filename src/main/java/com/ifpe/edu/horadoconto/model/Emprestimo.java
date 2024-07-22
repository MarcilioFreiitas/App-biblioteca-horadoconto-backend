package com.ifpe.edu.horadoconto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference("usuario-emprestimo")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @JsonBackReference("livro-emprestimo")
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column
    @NotNull
    private LocalDate dataRetirada;
    
    @Column
    @NotNull
    private  LocalDate dataDevolucao;
    
    @Column
    @NotNull
    private StatusEmprestimo statusEmprestimo;

	
    public Emprestimo() {
    	
    }

    public Emprestimo(Long id, Usuario usuario, Livro livro, @NotNull  LocalDate dataRetirada,
			@NotNull LocalDate dataDevolucao, @NotNull  StatusEmprestimo statusEmprestimo) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.livro = livro;
		this.dataRetirada = dataRetirada;
		this.dataDevolucao = dataDevolucao;
		this.statusEmprestimo = statusEmprestimo;
	}


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

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public @NotNull LocalDate getDataRetirada() {
		return dataRetirada;
	}

	public void setDataRetirada(@NotNull LocalDate dataRetirada) {
		this.dataRetirada = dataRetirada;
	}

	public @NotNull LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(@NotNull LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public  StatusEmprestimo getStatusEmprestimo() {
		return statusEmprestimo;
	}

	public void setStatusEmprestimo(StatusEmprestimo statusEmprestimo) {
		this.statusEmprestimo = statusEmprestimo;
	}
	
    

}
package com.ifpe.edu.horadoconto.model;

import java.util.Set;

import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class Livro {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column
    @NotNull
    private String titulo;
	
	@Column
    @NotNull
    private String autor;
	
	@Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private Genero genero;
	
	@Column(value = "TEXT")
	@NotNull
	private String sinopse;

	
	@Column
    @NotNull
    private String isbn;
	
	@Column
    private String imagem_capa;
	
	@Column
    @NotNull
    private Boolean disponibilidade;

	
	@JsonManagedReference("livro-emprestimo")
    @OneToMany(mappedBy = "livro")
    private Set<Emprestimo> emprestimos;

	
    
    
    
    public Livro(Long id, @NotNull String titulo, @NotNull String autor, @NotNull @NotNull Genero genero,
			@NotNull String sinopse, @NotNull String isbn, @NotNull String imagem_capa, @NotNull Boolean disponibilidade) {
		
    	super();
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.genero = genero;
		this.sinopse = sinopse;
		this.isbn = isbn;
		this.imagem_capa = imagem_capa;
		this.disponibilidade = disponibilidade;
		
	}
    
    public Livro() {
    }

	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public @NotNull Genero getGenero() {
		return genero;
	}

	public void setGenero(@NotNull Genero genero) {
		this.genero = genero;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getImagem_capa() {
		return imagem_capa;
	}

	public void setImagem_capa(String imagem_capa) {
		this.imagem_capa = imagem_capa;
	}

	public  Boolean getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(Boolean disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	public Set<Emprestimo> getEmprestimos() {
		return emprestimos;
	}

	public void setEmprestimos(Set<Emprestimo> emprestimos) {
		this.emprestimos = emprestimos;
	}

    // getters e setters
}
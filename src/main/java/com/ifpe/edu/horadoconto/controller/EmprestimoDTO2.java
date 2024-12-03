package com.ifpe.edu.horadoconto.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.ifpe.edu.horadoconto.model.StatusEmprestimo;

public record EmprestimoDTO2(
	    Long id,
	    String nomeUsuario,
	    String tituloLivro,
	    String imagem,
	    LocalDate dataRetirada,
	    LocalDate dataDevolucao,
	    StatusEmprestimo statusEmprestimo
	) {
	    public String getDataRetirada() {
	        return dataRetirada.format(DateTimeFormatter.ISO_LOCAL_DATE);
	    }

	    public String getDataDevolucao() {
	        return dataDevolucao.format(DateTimeFormatter.ISO_LOCAL_DATE);
	    }
	}
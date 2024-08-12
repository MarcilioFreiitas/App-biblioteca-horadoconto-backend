package com.ifpe.edu.horadoconto.controller;

import com.ifpe.edu.horadoconto.model.StatusEmprestimo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record EmprestimoDTO(
    Long id,
    String nomeUsuario,
    String tituloLivro,
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

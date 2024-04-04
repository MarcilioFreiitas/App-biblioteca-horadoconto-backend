package com.ifpe.edu.horadoconto.controller;

import com.ifpe.edu.horadoconto.model.Livro;
import com.ifpe.edu.horadoconto.model.StatusEmprestimo;
import com.ifpe.edu.horadoconto.model.Usuario;

public record EmprestimoDTO(Usuario usuario, Livro livro, String dataRetirada , String dataDevolucao, StatusEmprestimo statusEmprestimo) {

}

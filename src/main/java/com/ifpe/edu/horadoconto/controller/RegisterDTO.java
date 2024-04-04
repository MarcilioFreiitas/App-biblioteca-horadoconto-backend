package com.ifpe.edu.horadoconto.controller;

import com.ifpe.edu.horadoconto.model.UserRoles;

public record RegisterDTO(String nome, String sobreNome, String cpf, String email, String senha, UserRoles role) {

}

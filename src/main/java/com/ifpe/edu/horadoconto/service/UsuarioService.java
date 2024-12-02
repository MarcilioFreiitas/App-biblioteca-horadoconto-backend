package com.ifpe.edu.horadoconto.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpe.edu.horadoconto.model.StatusEmprestimo;
import com.ifpe.edu.horadoconto.repository.EmprestimoRepository;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public void excluirUsuario(Long usuarioId) {
        List<StatusEmprestimo> activeStatuses = Arrays.asList(StatusEmprestimo.PENDENTE, StatusEmprestimo.REJEITADO, StatusEmprestimo.DEVOLVIDO);
        boolean hasActiveLoan = emprestimoRepository.existsByUsuarioIdAndStatusEmprestimoIn(usuarioId, activeStatuses);

        if (hasActiveLoan) {
            throw new IllegalStateException("O usuário tem um empréstimo ativo ligado a ele");
        }

        usuarioRepository.deleteById(usuarioId);
    }
}



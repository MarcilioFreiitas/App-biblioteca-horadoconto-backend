package com.ifpe.edu.horadoconto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ifpe.edu.horadoconto.controller.EmprestimoDTO;
import com.ifpe.edu.horadoconto.model.Emprestimo;
import com.ifpe.edu.horadoconto.model.Livro;
import com.ifpe.edu.horadoconto.model.StatusEmprestimo;
import com.ifpe.edu.horadoconto.model.Usuario;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @Query("SELECT new com.ifpe.edu.horadoconto.controller.EmprestimoDTO(e.id, u.nome, l.titulo, e.dataRetirada, e.dataDevolucao, e.statusEmprestimo) " +
           "FROM Emprestimo e " +
           "JOIN e.usuario u " +
           "JOIN e.livro l")
    List<EmprestimoDTO> findAllEmprestimos();

    List<Emprestimo> findByUsuarioId(Long id);

    Optional<Emprestimo> findByUsuarioAndLivroAndStatusEmprestimo(Usuario usuario, Livro livro, StatusEmprestimo statusEmprestimo);
    
    boolean existsByUsuarioIdAndStatusEmprestimoIn(Long usuarioId, List<StatusEmprestimo> statusEmprestimo);}




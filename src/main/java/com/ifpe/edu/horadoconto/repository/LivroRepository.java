package com.ifpe.edu.horadoconto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpe.edu.horadoconto.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

	Optional<Livro> findByTituloAndAutor(String titulo, String autor);

}

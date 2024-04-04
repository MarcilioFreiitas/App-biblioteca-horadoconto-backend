package com.ifpe.edu.horadoconto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpe.edu.horadoconto.model.Multa;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long>{

}

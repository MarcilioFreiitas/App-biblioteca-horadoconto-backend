package com.ifpe.edu.horadoconto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.ifpe.edu.horadoconto.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	UserDetails	findByemail(String email);
	UserDetails findBysenha(String senha);
	Optional<Usuario> findById(Long id);
	Optional<Usuario> findByCpf(String cpf);
	Usuario findByEmail(String email);
	Usuario findBycpf (String cpf);

}

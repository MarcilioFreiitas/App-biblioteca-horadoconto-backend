package com.ifpe.edu.horadoconto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.ifpe.edu.horadoconto.model.Admin;



@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	UserDetails	findByemail(String email);
	UserDetails findBysenha(String senha);
	
}

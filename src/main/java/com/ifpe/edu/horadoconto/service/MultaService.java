package com.ifpe.edu.horadoconto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpe.edu.horadoconto.repository.MultaRepository;

@Service
public class MultaService {

	@Autowired
	MultaRepository multarepository;
	
	
}

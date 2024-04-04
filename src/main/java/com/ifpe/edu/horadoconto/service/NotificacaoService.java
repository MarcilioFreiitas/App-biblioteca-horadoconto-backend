package com.ifpe.edu.horadoconto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpe.edu.horadoconto.repository.NotificacaoRepository;

@Service
public class NotificacaoService {

	@Autowired
	NotificacaoRepository notificacaoRepository;
}

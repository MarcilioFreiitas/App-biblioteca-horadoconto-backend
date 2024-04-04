package com.ifpe.edu.horadoconto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.ifpe.edu.horadoconto.model.Emprestimo;

import com.ifpe.edu.horadoconto.service.EmprestimoService;

@RestController
@RequestMapping("/emprestimo")
public class EmprestimoController {

	@Autowired
	private EmprestimoService emprestimoService;
	
	@PostMapping("/emprestar")
	public ResponseEntity emprestar(@RequestBody Emprestimo emprestimo) {
	    Emprestimo novoEmprestimo = emprestimoService.emprestar(emprestimo);
	    return ResponseEntity.ok(novoEmprestimo);
	}
	
	@PutMapping("/alterarEmprestimo/{id}")
	public ResponseEntity alterarEmprestimo(@PathVariable Long id, @RequestBody Emprestimo emprestimo ) {
		 Emprestimo emprestimoAtualizado = emprestimoService.alterar(id, emprestimo);
		    return ResponseEntity.ok(emprestimoAtualizado);
	}
	
	
	@GetMapping("/listarEmprestimo")
	public ResponseEntity listarEmprestimo( String orderBy) {
	    List<Emprestimo> emprestimo = emprestimoService.listar(orderBy);
	    return ResponseEntity.ok(emprestimo);
	}
	
	@DeleteMapping("/apagarEmpresitmo/{id}")
	public ResponseEntity deletarEmprestimo(@PathVariable Long id) {
		emprestimoService.apagar(id);
	    return ResponseEntity.ok().build();
	}

	@PutMapping("/aprovar/{id}")
	public ResponseEntity<Emprestimo> aprovar(@PathVariable Long id) {
	    Emprestimo emprestimo = emprestimoService.aprovar(id);
	    return ResponseEntity.ok(emprestimo);
	}

	@PutMapping("/rejeitar/{id}")
	public ResponseEntity<Emprestimo> rejeitar(@PathVariable Long id) {
	    Emprestimo emprestimo = emprestimoService.rejeitar(id);
	    return ResponseEntity.ok(emprestimo);
	}
	
	
	
}

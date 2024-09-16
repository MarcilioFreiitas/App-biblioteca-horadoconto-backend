package com.ifpe.edu.horadoconto.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.bind.annotation.*;
import com.ifpe.edu.horadoconto.model.Livro;
import com.ifpe.edu.horadoconto.service.LivroService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/livros")
public class LivroController {

	@Autowired
	private LivroService livroService;
	
	public LivroController(LivroService livroService) {
		this.livroService = livroService;
	}

	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestParam("autor") String autor,
	                             @RequestParam("titulo") String titulo,
	                             @RequestParam("genero") String genero,
	                             @RequestParam("disponibilidade") boolean disponibilidade,
	                             @RequestParam("sinopse") String sinopse,
	                             @RequestParam("isbn") String isbn,
	                             @RequestParam("imagem_capa") MultipartFile imagem) {
	    Livro novoLivro = livroService.criarLivro(autor, titulo, genero, disponibilidade, sinopse, isbn, imagem);
	    return ResponseEntity.ok(novoLivro);
	}

	@PutMapping("/alterar/{id}")
	public ResponseEntity alterar(@PathVariable Long id, @RequestBody Livro livro) {
	    Livro livroAtualizado = livroService.alterar(id, livro);
	    return ResponseEntity.ok(livroAtualizado);
	}

	@DeleteMapping("/apagar/{id}")
	public ResponseEntity apagar(@PathVariable Long id) {
	    livroService.apagar(id);
	    return ResponseEntity.ok().build();
	}

	@GetMapping("/listar")
	public ResponseEntity listar(@RequestParam(required = false, defaultValue = "") String orderBy) {
	    List<Livro> livros = livroService.listar(orderBy);
	    return ResponseEntity.ok(livros);
	}
	
	@GetMapping("/buscar/{id}")
    public ResponseEntity listarPorId(@PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        if (livro != null) {
            return ResponseEntity.ok(livro);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
        }
    }
	

	

}

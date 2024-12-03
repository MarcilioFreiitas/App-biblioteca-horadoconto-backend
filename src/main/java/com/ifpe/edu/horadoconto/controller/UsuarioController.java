package com.ifpe.edu.horadoconto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;
import com.ifpe.edu.horadoconto.service.UsuarioService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/salvar")
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {
    	Usuario novoUsuario = usuarioRepository.save(usuario);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    @PatchMapping("/alterar/{id}")
    public ResponseEntity<Usuario> alterar(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "nome":
                    usuario.setNome((String) value);
                    break;
                case "sobreNome":
                    usuario.setSobreNome((String) value);
                    break;
                case "cpf":
                    usuario.setCpf((String) value);
                    break;
                case "email":
                    usuario.setEmail((String) value);
                    break;
                // Adicione outros campos conforme necessário
            }
        });

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return new ResponseEntity<>(usuarioAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/apagar/{id}") public ResponseEntity<String> apagar(@PathVariable Long id) 
    { try { 
    	usuarioService.excluirUsuario(id); 
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	} catch (IllegalStateException e) { 
    		return ResponseEntity.badRequest().body(e.getMessage());
    		}
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
            .map(usuario -> new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobreNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getEmprestimos().stream()
                    .map(emprestimo -> new EmprestimoDTO(
                        emprestimo.getId(), 
                        emprestimo.getUsuario().getNome(), 
                        emprestimo.getLivro().getTitulo(), 
                        
                        emprestimo.getDataRetirada(), 
                        emprestimo.getDataDevolucao(), 
                        emprestimo.getStatusEmprestimo())
                    ).collect(Collectors.toList())
            )).collect(Collectors.toList());
        return new ResponseEntity<>(usuariosDTO, HttpStatus.OK);
    }


}

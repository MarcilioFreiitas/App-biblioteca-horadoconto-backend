package com.ifpe.edu.horadoconto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ifpe.edu.horadoconto.model.Usuario;
import com.ifpe.edu.horadoconto.repository.UsuarioRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
}

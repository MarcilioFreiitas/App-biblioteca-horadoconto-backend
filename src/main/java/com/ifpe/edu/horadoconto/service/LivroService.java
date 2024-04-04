package com.ifpe.edu.horadoconto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ifpe.edu.horadoconto.ResourceNotFoundException;
import com.ifpe.edu.horadoconto.model.Livro;
import com.ifpe.edu.horadoconto.repository.LivroRepository;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    public Livro criarLivro(String autor, String titulo, String genero, boolean disponibilidade, String sinopse, String isbn, MultipartFile imagem) {
        // Verifique se o livro já existe
        Optional<Livro> livroExistente = repository.findByTituloAndAutor(titulo, autor);
        if (livroExistente.isPresent()) {
            throw new IllegalArgumentException("Livro com o mesmo título e autor já existe");
        }

        // Crie um novo objeto Livro e defina seus campos
        Livro livro = new Livro();
        livro.setAutor(autor);
        livro.setTitulo(titulo);
        livro.setGenero(genero);
        livro.setDisponibilidade(disponibilidade);
        livro.setSinopse(sinopse);
        livro.setIsbn(isbn);

        // Aqui você pode adicionar o código para salvar a imagem e definir o caminho da imagem
        try {
            // Salva a imagem no diretório
            Path filePath = Paths.get("src/main/resources/static/imagens/capas/" + imagem.getOriginalFilename());
            Files.write(filePath, imagem.getBytes());

            // Atualiza o caminho da imagem da capa do livro
            livro.setImagem_capa("/imagens/capas/" + imagem.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao fazer upload da imagem", e);
        }


        // Salve o livro e retorne
        return this.repository.save(livro);
    }


    public Livro alterar(Long id, Livro livro) {
        Livro livroExistente = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
        if (!livroExistente.equals(livro)) {
            livroExistente.setTitulo(livro.getTitulo());
            livroExistente.setAutor(livro.getAutor());
            livroExistente.setGenero(livro.getGenero());
            livroExistente.setDisponibilidade(livro.getDisponibilidade());
            livroExistente.setSinopse(livro.getSinopse());
            livroExistente.setIsbn(livro.getIsbn());
            livroExistente.setImagem_capa(livro.getImagem_capa());
            // atualizar outros campos...
            return repository.save(livroExistente);
        }
        return livroExistente;
    }

    public void apagar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        this.repository.deleteById(id);
    }

    public List<Livro> listar(String orderBy) {
        if (orderBy.equals("titulo")) {
            return this.repository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
        } else if (orderBy.equals("autor")) {
            return this.repository.findAll(Sort.by(Sort.Direction.ASC, "autor"));
        } else if (orderBy.equals("genero")) {
            return this.repository.findAll(Sort.by(Sort.Direction.ASC, "genero"));
        } else {
            return this.repository.findAll();
        }
    }
    
 // Dentro do seu LivroService
    public Livro buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }


}


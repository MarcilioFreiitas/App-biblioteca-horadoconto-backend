package com.ifpe.edu.horadoconto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ifpe.edu.horadoconto.exception.ResourceNotFoundException;
import com.ifpe.edu.horadoconto.model.Genero;
import com.ifpe.edu.horadoconto.model.Livro;
import com.ifpe.edu.horadoconto.repository.LivroRepository;

import jakarta.validation.constraints.NotNull;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    public Livro criarLivro(String autor, String titulo, @NotNull Genero genero, boolean disponibilidade, String sinopse, String isbn, MultipartFile imagem) {
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
            // Defina o caminho absoluto para salvar a imagem dentro do contêiner
            String pastaUpload = "/usr/share/app/imagens/capas/";
            Path filePath = Paths.get(pastaUpload + imagem.getOriginalFilename());

            // Crie o diretório se não existir
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            // Salva a imagem no diretório
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


    public Livro alterar(Long id, String titulo, String autor, Genero genero, String sinopse, String isbn, boolean disponibilidade, MultipartFile imagemCapa) {
        Livro livroExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        livroExistente.setTitulo(titulo);
        livroExistente.setAutor(autor);
        livroExistente.setGenero(genero);
        livroExistente.setDisponibilidade(disponibilidade);
        livroExistente.setSinopse(sinopse);
        livroExistente.setIsbn(isbn);

        // Se uma nova imagem de capa foi enviada, substitua a existente
        if (imagemCapa != null && !imagemCapa.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(imagemCapa.getOriginalFilename());
                Path uploadPath = Paths.get("src/main/resources/static/imagens/capas/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Excluir imagem antiga se existir
                if (livroExistente.getImagem_capa() != null) {
                    Path oldImagePath = Paths.get("src/main/resources/static" + livroExistente.getImagem_capa());
                    Files.deleteIfExists(oldImagePath);
                }

                // Salva a nova imagem
                Files.copy(imagemCapa.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                livroExistente.setImagem_capa("/imagens/capas/" + fileName); // Ajuste o caminho conforme necessário
            } catch (Exception e) {
                throw new RuntimeException("Falha ao salvar a imagem", e);
            }
        }

        return repository.save(livroExistente);
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


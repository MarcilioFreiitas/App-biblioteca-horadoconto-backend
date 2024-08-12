package com.ifpe.edu.horadoconto.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ifpe.edu.horadoconto.controller.EmprestimoDTO;
import com.ifpe.edu.horadoconto.model.Emprestimo;
import com.ifpe.edu.horadoconto.model.Livro;
import com.ifpe.edu.horadoconto.model.StatusEmprestimo;
import com.ifpe.edu.horadoconto.repository.EmprestimoRepository;
import com.ifpe.edu.horadoconto.repository.LivroRepository;

@Service
public class EmprestimoService {

	@Autowired
	EmprestimoRepository emprestimorepository;
	
	@Autowired
	LivroRepository livroRepository;

	public Emprestimo emprestar(Emprestimo emprestimo) {
	    // Verificar se o livro está associado ao empréstimo
	    if (emprestimo.getLivro() == null) {
	        throw new IllegalArgumentException("O livro associado ao empréstimo não pode ser null.");
	    }

	    // Verificar se o livro está disponível para empréstimo
	    Livro livro = livroRepository.findById(emprestimo.getLivro().getId())
	            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

	    if (!livro.getDisponibilidade()) {
	        throw new RuntimeException("Livro não está disponível para empréstimo");
	    }

	    
	    livroRepository.save(livro);

	    // Salvar o empréstimo
	    return emprestimorepository.save(emprestimo);
	}


	public Emprestimo alterar(Long id, Emprestimo emprestimo) {
		// Buscar o empréstimo existente
	    Emprestimo emprestimoExistente = emprestimorepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

	    // Atualizar os campos do empréstimo existente
	    emprestimoExistente.setUsuario(emprestimo.getUsuario());
	    emprestimoExistente.setLivro(emprestimo.getLivro());
	    emprestimoExistente.setDataRetirada(emprestimo.getDataRetirada());
	    emprestimoExistente.setDataDevolucao(emprestimo.getDataDevolucao());
	    emprestimoExistente.setStatusEmprestimo(emprestimo.getStatusEmprestimo());

	    // Salvar o empréstimo atualizado
	    emprestimorepository.save(emprestimoExistente);

	    // Retornar o empréstimo atualizado
	    return emprestimoExistente;
	}

	  public List<EmprestimoDTO> listar(String orderBy) {
	        // Buscar todos os empréstimos
	        List<EmprestimoDTO> emprestimos = emprestimorepository.findAllEmprestimos();

	        // Ordenar a lista de empréstimos se necessário
	        if (orderBy != null) {
	            switch (orderBy) {
	                case "dataRetirada":
	                    emprestimos.sort(Comparator.comparing(EmprestimoDTO::dataRetirada));
	                    break;
	                case "dataDevolucao":
	                    emprestimos.sort(Comparator.comparing(EmprestimoDTO::dataDevolucao));
	                    break;
	                // Adicione mais campos para ordenação conforme necessário
	            }
	        }

	        // Retornar a lista de empréstimos
	        return emprestimos;
	    }

	public void apagar(Long id) {
		 // Verificar se o empréstimo existe
	    if (!emprestimorepository.existsById(id)) {
	        throw new RuntimeException("Empréstimo não encontrado");
	    }

	    // Apagar o empréstimo
	    emprestimorepository.deleteById(id);
		
	}
	
	    @Transactional
	    public Emprestimo aprovar(Long id) {
	        Emprestimo emprestimo = emprestimorepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

	        Livro livro = livroRepository.findById(emprestimo.getLivro().getId())
	                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

	        if (!livro.getDisponibilidade()) {
	            throw new RuntimeException("Livro não está disponível para empréstimo");
	        }

	        // Atualizar o status do empréstimo e do livro
	        emprestimo.setStatusEmprestimo(StatusEmprestimo.EMPRESTADO);
	        livro.setDisponibilidade(false);

	        // Salvar as alterações
	        livroRepository.save(livro);
	        return emprestimorepository.save(emprestimo);
	    }

    public Emprestimo rejeitar(Long id) {
        Emprestimo emprestimo = emprestimorepository.findById(id).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
        emprestimo.setStatusEmprestimo(StatusEmprestimo.REJEITADO);
        return emprestimorepository.save(emprestimo);
    }


    public List<EmprestimoDTO> listarPorUsuario(Long id) {
        // Consulte o banco de dados para obter os empréstimos do usuário com o ID fornecido
        List<Emprestimo> emprestimos = emprestimorepository.findByUsuarioId(id);

        // Mapeie os resultados para objetos EmprestimoDTO
        return emprestimos.stream()
                .map(this::mapToEmprestimoDTO)
                .collect(Collectors.toList());
    }
    
    @Scheduled(cron = "0 0 0 * * ?") // Executa diariamente à meia-noite
    public void verificarEmprestimosAtrasados() {
        List<Emprestimo> emprestimos = emprestimorepository.findAll();

        LocalDate dataAtual = LocalDate.now();

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getStatusEmprestimo() == StatusEmprestimo.EMPRESTADO
                    && emprestimo.getDataDevolucao().isBefore(dataAtual)) {
                emprestimo.setStatusEmprestimo(StatusEmprestimo.ATRASADO);
                emprestimorepository.save(emprestimo);
            }
        }
    }
    
    private EmprestimoDTO mapToEmprestimoDTO(Emprestimo emprestimo) {
        // Map relevant fields from Emprestimo to EmprestimoDTO
        return new EmprestimoDTO(
                emprestimo.getId(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataRetirada(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatusEmprestimo()
        );
    }

}

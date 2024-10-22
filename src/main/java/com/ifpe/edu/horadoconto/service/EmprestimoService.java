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
import com.ifpe.edu.horadoconto.exception.ResourceNotFoundException;
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

	    LocalDate dataEmprestimo = emprestimo.getDataRetirada();
	    LocalDate dataMaximaDevolucao = dataEmprestimo.plusMonths(1);

	    if (emprestimo.getDataDevolucao().isAfter(dataMaximaDevolucao)) {
	        throw new IllegalArgumentException("A data de devolução não pode ultrapassar um mês a partir da data de empréstimo.");
	    }
	    
	    
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
    public Emprestimo devolver(Long id) {
        Emprestimo emprestimo = emprestimorepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com o ID: " + id));
        
        // Altere o status do empréstimo para "devolvido"
        emprestimo.setStatusEmprestimo(StatusEmprestimo.DEVOLVIDO);
        
        // Atualize a disponibilidade do livro para "disponível"
        Livro livro = emprestimo.getLivro();
        livro.setDisponibilidade(true);
        
        // Salve as alterações no banco de dados
        emprestimorepository.save(emprestimo);
        livroRepository.save(livro);
        
        return emprestimo;
    }

    public Emprestimo renovar(Long id, LocalDate novaDataDevolucao) {
        // Busca o empréstimo pelo ID
        Emprestimo emprestimo = emprestimorepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com o ID: " + id));

        // Verifica se o status permite renovação (por exemplo, "emprestado")
        if (emprestimo.getStatusEmprestimo() != StatusEmprestimo.EMPRESTADO) {
            throw new IllegalStateException("Não é possível renovar um empréstimo com status diferente de 'emprestado'.");
        }

        // Verifica se a nova data de devolução não ultrapassa um mês a partir da data de empréstimo original
        LocalDate dataEmprestimo = emprestimo.getDataRetirada();
        LocalDate dataMaximaDevolucao = dataEmprestimo.plusMonths(1);

        if (novaDataDevolucao.isAfter(dataMaximaDevolucao)) {
            throw new IllegalStateException("A nova data de devolução não pode ultrapassar um mês a partir da data de empréstimo.");
        }

        // Atualiza a data de devolução
        emprestimo.setDataDevolucao(novaDataDevolucao);

        // Salva as alterações no banco de dados
        return emprestimorepository.save(emprestimo);
    }


    

}

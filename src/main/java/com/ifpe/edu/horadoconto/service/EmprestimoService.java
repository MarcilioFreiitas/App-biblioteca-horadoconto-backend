package com.ifpe.edu.horadoconto.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.ifpe.edu.horadoconto.controller.EmprestimoDTO;
import com.ifpe.edu.horadoconto.controller.EmprestimoDTO2;
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
	
	@Autowired
	EmailService emailService;

	public Emprestimo emprestar(Emprestimo emprestimo) {
	    if (emprestimo.getLivro() == null) {
	        throw new IllegalArgumentException("O livro associado ao empréstimo não pode ser null.");
	    }

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

	    // Verificar se já existe um empréstimo pendente para o mesmo livro e usuário
	    Optional<Emprestimo> emprestimoPendente = emprestimorepository
	            .findByUsuarioAndLivroAndStatusEmprestimo(emprestimo.getUsuario(), emprestimo.getLivro(), StatusEmprestimo.PENDENTE);

	    if (emprestimoPendente.isPresent()) {
	        throw new RuntimeException("Já existe um empréstimo pendente para este livro");
	    }

	    livroRepository.save(livro);

	    return emprestimorepository.save(emprestimo);
	}




	public Emprestimo alterar(Long id, Emprestimo emprestimo) {
	
	    Emprestimo emprestimoExistente = emprestimorepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

	    
	    emprestimoExistente.setUsuario(emprestimo.getUsuario());
	    emprestimoExistente.setLivro(emprestimo.getLivro());
	    emprestimoExistente.setDataRetirada(emprestimo.getDataRetirada());
	    emprestimoExistente.setDataDevolucao(emprestimo.getDataDevolucao());
	    emprestimoExistente.setStatusEmprestimo(emprestimo.getStatusEmprestimo());

	    
	    emprestimorepository.save(emprestimoExistente);

	   
	    return emprestimoExistente;
	}

	  public List<EmprestimoDTO> listar(String orderBy) {
	       
	        List<EmprestimoDTO> emprestimos = emprestimorepository.findAllEmprestimos();

	       
	        if (orderBy != null) {
	            switch (orderBy) {
	                case "dataRetirada":
	                    emprestimos.sort(Comparator.comparing(EmprestimoDTO::dataRetirada));
	                    break;
	                case "dataDevolucao":
	                    emprestimos.sort(Comparator.comparing(EmprestimoDTO::dataDevolucao));
	                    break;
	                
	            }
	        }

	        
	        return emprestimos;
	    }

	public void apagar(Long id) {
		 
	    if (!emprestimorepository.existsById(id)) {
	        throw new RuntimeException("Empréstimo não encontrado");
	    }

	  
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

	    emprestimo.setStatusEmprestimo(StatusEmprestimo.EMPRESTADO);
	    livro.setDisponibilidade(false);

	    livroRepository.save(livro);
	    Emprestimo emprestimoAprovado = emprestimorepository.save(emprestimo);

	    // Enviar e-mail de confirmação
	    String to = emprestimo.getUsuario().getEmail();
	    String subject = "Confirmação de Empréstimo de Livro";
	    String text = "Olá " + emprestimo.getUsuario().getNome() + ",\n\n" +
	                  "Sua solicitação de empréstimo do livro " + emprestimo.getLivro().getTitulo() + " foi aprovada.\n" +
	                  "O status do seu empréstimo agora é Ativo.\n\n" +
	                  "Atenciosamente,\n" +
	                  "Equipe da Biblioteca IFPE - Palmares";

	    emailService.sendEmail(to, subject, text);

	    return emprestimoAprovado;
	}


    public Emprestimo rejeitar(Long id) {
        Emprestimo emprestimo = emprestimorepository.findById(id).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
        emprestimo.setStatusEmprestimo(StatusEmprestimo.REJEITADO);
        return emprestimorepository.save(emprestimo);
    }


    public List<EmprestimoDTO2> listarPorUsuario(Long id) {
        
        List<Emprestimo> emprestimos = emprestimorepository.findByUsuarioId(id);

        
        return emprestimos.stream()
                .map(this::mapToEmprestimoDTO2)
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
       
        return new EmprestimoDTO(
                emprestimo.getId(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getLivro().getTitulo(),
               
                emprestimo.getDataRetirada(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatusEmprestimo()
        );
    }
    
    private EmprestimoDTO2 mapToEmprestimoDTO2(Emprestimo emprestimo) {
        
        return new EmprestimoDTO2(
                emprestimo.getId(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getLivro().getImagem_capa(),
               
                emprestimo.getDataRetirada(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatusEmprestimo()
        );
    }
    
    public Emprestimo devolver(Long id) {
        Emprestimo emprestimo = emprestimorepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com o ID: " + id));
        
       
        emprestimo.setStatusEmprestimo(StatusEmprestimo.DEVOLVIDO);
        
       
        Livro livro = emprestimo.getLivro();
        livro.setDisponibilidade(true);
        
       
        emprestimorepository.save(emprestimo);
        livroRepository.save(livro);
        
        return emprestimo;
    }

    public Emprestimo renovar(Long id, LocalDate novaDataDevolucao) {
        Emprestimo emprestimo = emprestimorepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com o ID: " + id));

        
        if (emprestimo.getStatusEmprestimo() != StatusEmprestimo.EMPRESTADO) {
            throw new IllegalStateException("Não é possível renovar um empréstimo com status diferente de 'emprestado'.");
        }

       
        LocalDate dataEmprestimo = emprestimo.getDataRetirada();
        LocalDate dataMaximaDevolucao = dataEmprestimo.plusMonths(1);

        if (novaDataDevolucao.isAfter(dataMaximaDevolucao)) {
            throw new IllegalStateException("A nova data de devolução não pode ultrapassar um mês a partir da data de empréstimo.");
        }

        
        emprestimo.setDataDevolucao(novaDataDevolucao);

       
        return emprestimorepository.save(emprestimo);
    }


    

}

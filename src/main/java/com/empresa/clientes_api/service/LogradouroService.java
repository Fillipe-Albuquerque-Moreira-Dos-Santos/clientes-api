package com.empresa.clientes_api.service;

import com.empresa.clientes_api.model.Logradouro;
import com.empresa.clientes_api.repository.LogradouroRepository;
import com.empresa.clientes_api.repository.LogradouroRepositoryImpl;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LogradouroService {

    private final LogradouroRepository logradouroRepository;
    private final LogradouroRepositoryImpl logradouroRepositoryProcedure;

    public List<Logradouro> listarTodos() {
        return logradouroRepository.findAll();
    }

    public Logradouro salvar(Logradouro logradouro) {
        return logradouroRepository.save(logradouro);
    }

    public Logradouro editar(Long id, Logradouro logradouro) {
        return logradouroRepository.save(logradouro);
    }
    @Transactional
    public void excluir(Long id) {
        Optional<Logradouro> logradouroOpt = logradouroRepository.findById(id);
        if (!logradouroOpt.isPresent()) {
            throw new EmptyResultDataAccessException("Logradouro não encontrado", 1);
        }

        Logradouro logradouro = logradouroOpt.get();

        if (logradouro.getCliente() != null) {
            throw new IllegalStateException("Não é possível excluir: logradouro vinculado a um cliente.");
        }

        logradouroRepository.deleteById(id);
    }


}

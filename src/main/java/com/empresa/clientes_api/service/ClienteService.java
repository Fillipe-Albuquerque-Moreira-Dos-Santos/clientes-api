package com.empresa.clientes_api.service;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.model.Logradouro;
import com.empresa.clientes_api.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteDTO criarCliente(String nome, String email, List<String> logradouros, MultipartFile logotipo) {
        // Verificando se o cliente já existe pelo email
        if (clienteRepository.existsByEmail(email)) {
            throw new RuntimeException("Cliente com o e-mail já cadastrado.");
        }

        // Criando um novo cliente
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);

        // Salvando o logotipo como byte[] no banco de dados
        if (logotipo != null) {
            try {
                cliente.setLogotipo(logotipo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar o logotipo.", e);
            }
        }

        // Criando a lista de logradouros
        if (logradouros != null && !logradouros.isEmpty()) {
            List<Logradouro> logradourosList = logradouros.stream()
                    .map(endereco -> new Logradouro(endereco, cliente))  // Aqui estamos passando o endereco e cliente
                    .collect(Collectors.toList());
            cliente.setLogradouros(logradourosList);
        }

        // Salvando o cliente no banco de dados
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // Retornando o DTO com os dados salvos
        return new ClienteDTO(clienteSalvo.getNome(), clienteSalvo.getEmail(),
                clienteSalvo.getLogradouros().stream().map(Logradouro::getEndereco).collect(Collectors.toList()), null);
    }







    public Cliente obterCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizarCliente(Long id, ClienteDTO clienteDTO) throws IOException {
        Cliente cliente = obterCliente(id);

        // Verificando se o e-mail foi alterado e se já existe no banco
        if (!cliente.getEmail().equals(clienteDTO.getEmail()) && clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Cliente com o e-mail já cadastrado.");
        }

        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());

        // Se o logotipo foi enviado, convertemos ele para bytes
        if (clienteDTO.getLogotipo() != null) {
            cliente.setLogotipo(clienteDTO.getLogotipo().getBytes());
        }

        return clienteRepository.save(cliente);
    }

    public boolean removerCliente(Long id) {
        Cliente cliente = obterCliente(id);
        clienteRepository.delete(cliente);
        return true; // Sucesso na remoção
    }

    // Método para upload de logotipo
    public void uploadLogotipo(Long id, MultipartFile logotipo) {
        Cliente cliente = obterCliente(id);

        try {
            // Verificando se o arquivo de logotipo não é nulo
            if (logotipo != null) {
                cliente.setLogotipo(logotipo.getBytes());
                clienteRepository.save(cliente);
            } else {
                throw new RuntimeException("Arquivo de logotipo não pode ser nulo.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o logotipo: " + e.getMessage());
        }
    }
}

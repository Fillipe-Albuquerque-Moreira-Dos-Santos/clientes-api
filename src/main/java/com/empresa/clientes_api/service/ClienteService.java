package com.empresa.clientes_api.service;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente criarCliente(ClienteDTO clienteDTO) {
        // Verificando se o cliente já existe pelo email
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Cliente com o e-mail já cadastrado.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());

        // Se o logotipo foi enviado, convertemos ele para bytes
        if (clienteDTO.getLogotipo() != null) {
            cliente.setLogotipo(clienteDTO.getLogotipo().getBytes());
        }

        return clienteRepository.save(cliente);
    }

    public Cliente obterCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizarCliente(Long id, ClienteDTO clienteDTO) {
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

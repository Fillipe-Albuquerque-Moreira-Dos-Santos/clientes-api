package com.empresa.clientes_api.service;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente criarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setLogotipo(clienteDTO.getLogotipo());
        return clienteRepository.save(cliente);
    }

    public Cliente obterCliente(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = obterCliente(id);
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setLogotipo(clienteDTO.getLogotipo());
        return clienteRepository.save(cliente);
    }

    public void removerCliente(Long id) {
        Cliente cliente = obterCliente(id);
        clienteRepository.delete(cliente);
    }
}

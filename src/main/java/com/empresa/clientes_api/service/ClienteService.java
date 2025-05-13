package com.empresa.clientes_api.service;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.model.Logradouro;
import com.empresa.clientes_api.repository.ClienteRepository;
import com.empresa.clientes_api.repository.LogradouroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final LogradouroRepository logradouroRepository;

    public ClienteDTO criarCliente(String nome, String email, List<String> logradouros, MultipartFile logotipo) {
        if (clienteRepository.existsByEmail(email)) {
            throw new RuntimeException("Cliente com o e-mail já cadastrado.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);

        if (logotipo != null && !logotipo.isEmpty()) {
            try {
                cliente.setLogotipo(logotipo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar o logotipo.", e);
            }
        }

        // Primeiro salva o cliente sem logradouros para obter o ID
        Cliente salvo = clienteRepository.save(cliente);

        List<Logradouro> listaLogradouros = logradouros.stream()
                .map(nomeLogradouro -> {
                    Logradouro logradouro = logradouroRepository.findByLogradouro(nomeLogradouro)
                            .orElseThrow(() -> new RuntimeException("Logradouro não encontrado: " + nomeLogradouro));
                    logradouro.setCliente(salvo); // Vínculo essencial aqui
                    return logradouro;
                })
                .collect(Collectors.toList());

        // Atualiza os logradouros com o cliente associado
        logradouroRepository.saveAll(listaLogradouros);

        // Se quiser manter bidirecional, pode setar os logradouros no cliente
        salvo.setLogradouros(listaLogradouros);

        return new ClienteDTO(salvo);
    }




    // Método para encontrar todos os clientes
    public List<ClienteDTO> findAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll(); // Obtém todos os clientes do repositório
        return clientes.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }


    public Cliente obterCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO, boolean logradouroOnly) throws IOException {
        Cliente cliente = obterCliente(id);

        if (!logradouroOnly) {
            // Se e-mail mudou, verificar duplicidade
            if (!cliente.getEmail().equalsIgnoreCase(clienteDTO.getEmail()) &&
                    clienteRepository.existsByEmail(clienteDTO.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }

            cliente.setNome(clienteDTO.getNome());
            cliente.setEmail(clienteDTO.getEmail());

            if (clienteDTO.getLogotipo() != null && !clienteDTO.getLogotipo().isEmpty()) {
                cliente.setLogotipo(clienteDTO.getLogotipo().getBytes());
            }
        }

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return new ClienteDTO(clienteAtualizado);
    }

    public boolean deletar(Long id) {
        Cliente cliente = obterCliente(id);
        clienteRepository.delete(cliente);
        return true;
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

    // Buscar cliente por ID
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
    }
}
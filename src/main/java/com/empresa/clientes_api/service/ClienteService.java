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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

        // Salva o cliente para obter ID
        Cliente salvo = clienteRepository.save(cliente);

        // Remove duplicados enviados
        Set<String> logradourosUnicos = new HashSet<>(logradouros);

        List<Logradouro> listaLogradouros = logradourosUnicos.stream()
                .map(nomeLogradouro -> {
                    // Busca se já existe um logradouro com esse nome
                    Optional<Logradouro> existenteOpt = logradouroRepository.findByLogradouro(nomeLogradouro);

                    if (existenteOpt.isPresent()) {
                        Logradouro existente = existenteOpt.get();
                        if (existente.getCliente() == null) {
                            // Reutiliza o logradouro "órfão"
                            existente.setCliente(salvo);
                            return existente;
                        } else {
                            // Já está vinculado a outro cliente
                            throw new RuntimeException("O logradouro \"" + nomeLogradouro + "\" já está vinculado a outro cliente.");
                        }
                    }

                    // Se não existe, cria um novo
                    Logradouro novo = new Logradouro();
                    novo.setLogradouro(nomeLogradouro);
                    novo.setCliente(salvo);
                    return novo;
                })
                .collect(Collectors.toList());

        logradouroRepository.saveAll(listaLogradouros);
        salvo.setLogradouros(listaLogradouros);

        return new ClienteDTO(salvo);
    }



    // Método para encontrar todos os clientes
    public List<ClienteDTO> findAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }


    public Cliente obterCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO) throws IOException {
        Cliente cliente = obterCliente(id);

        String novoEmail = clienteDTO.getEmail();
        if (novoEmail != null && !novoEmail.equalsIgnoreCase(cliente.getEmail())) {
            Cliente existente = clienteRepository.findByEmail(novoEmail);
            if (existente != null && !existente.getId().equals(id)) {
                throw new IllegalArgumentException("E-mail já está em uso por outro cliente.");
            }
        }

        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(novoEmail);

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
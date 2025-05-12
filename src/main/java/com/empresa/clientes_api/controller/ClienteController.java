package com.empresa.clientes_api.controller;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.dto.LogradouroDTO;
import com.empresa.clientes_api.exception.ClienteJaCadastradoException;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:8081")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping(value = "/salvar-cliente", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteDTO> salvarCliente(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("logradouro") String logradouro,
            @RequestParam(value = "file", required = false) MultipartFile logotipo) {

        ClienteDTO clienteDTO = clienteService.criarCliente(nome, email, Collections.singletonList(logradouro), logotipo);

        // Retorna o DTO com os dados do cliente salvo
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    }

    // Listar todos os clientes
    @GetMapping("/listar-todos")
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<ClienteDTO> clientes = clienteService.findAllClientes();
        return ResponseEntity.ok(clientes);
    }

    // Carregar cliente por ID
    @GetMapping("/carregar-clientes/{id}")
    public ResponseEntity<?> carregarCliente(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.obterCliente(id);
            return ResponseEntity.ok(new ClienteDTO(cliente));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Cliente não encontrado"));
        }
    }

    // Atualizar cliente
    @PutMapping("/atualizar-cliente/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO atualizado = clienteService.atualizarCliente(id, clienteDTO, false);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao atualizar cliente: " + e.getMessage()));
        }
    }

    /**
     * Endpoint específico para atualizar apenas o logradouro de um cliente
     */
    @PutMapping("/{id}/atualizar-logradouro")
    public ResponseEntity<?> atualizarLogradouro(
            @PathVariable Long id,
            @RequestBody LogradouroDTO logradouroDTO) {
        try {
            ClienteDTO atualizado = clienteService.atualizarLogradouro(id, logradouroDTO);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao atualizar logradouro: " + e.getMessage()));
        }
    }

    // Excluir cliente
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> excluirCliente(@PathVariable Long id) {
        try {
            boolean resultado = clienteService.deletar(id);
            if (resultado) {
                return ResponseEntity.ok(Map.of("mensagem", "Cliente excluído com sucesso"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Cliente não encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao excluir cliente: " + e.getMessage()));
        }
    }

    // Upload de logotipo para cliente
    @PostMapping(value = "/{id}/upload-logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadLogotipo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            clienteService.uploadLogotipo(id, file);
            return ResponseEntity.ok(Map.of("mensagem", "Logotipo atualizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao atualizar logotipo: " + e.getMessage()));
        }
    }
}
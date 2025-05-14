package com.empresa.clientes_api.controller;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
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
            @RequestParam("logradouro") List<String> logradouros,
            @RequestParam(value = "file", required = false) MultipartFile logotipo) {

        // Aqui você já recebe a lista diretamente, não precisa converter
        ClienteDTO clienteDTO = clienteService.criarCliente(nome, email, logradouros, logotipo);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    }

    // Listar todos os clientes
    @GetMapping("/listar-todos")
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<ClienteDTO> clientes = clienteService.findAllClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/carregar-cliente/{id}")
    public ResponseEntity<?> carregarCliente(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.obterCliente(id);

            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Cliente não encontrado"));
            }

            return ResponseEntity.ok(new ClienteDTO(cliente));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro inesperado: " + e.getMessage()));
        }
    }


    // Atualizar cliente com suporte para multipart/form-data
    @PutMapping("/atualizar-cliente/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable Long id,
            @ModelAttribute ClienteDTO clienteDTO) {
        try {
            ClienteDTO atualizado = clienteService.atualizarCliente(id, clienteDTO);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao atualizar cliente: " + e.getMessage()));
        }
    }


    // Excluir cliente
    @DeleteMapping("excluir-cliente/{id}")
    public ResponseEntity<?> excluirCliente(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
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

    // Endpoint para obter logotipo de um cliente
    @GetMapping("/{id}/logo")
    public ResponseEntity<byte[]> obterLogotipo(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        byte[] imagem = cliente.getLogotipo();

        if (imagem == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // ou JPEG se for o caso
        return new ResponseEntity<>(imagem, headers, HttpStatus.OK);
    }

}
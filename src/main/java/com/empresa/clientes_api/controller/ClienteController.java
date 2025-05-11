package com.empresa.clientes_api.controller;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/clientes")
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


    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = Optional.ofNullable(clienteService.obterCliente(id));
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
//        Cliente cliente = clienteService.atualizarCliente(id, clienteDTO);
//        return ResponseEntity.ok(cliente);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCliente(@PathVariable Long id) {
        boolean isRemoved = clienteService.removerCliente(id);
        return isRemoved ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Exemplo de um método de upload de imagem para o logotipo
    @PostMapping("/{id}/upload-logo")
    public ResponseEntity<String> uploadLogotipo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            clienteService.uploadLogotipo(id, file);
            return ResponseEntity.status(HttpStatus.OK).body("Logotipo carregado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao carregar o logotipo.");
        }
    }
}
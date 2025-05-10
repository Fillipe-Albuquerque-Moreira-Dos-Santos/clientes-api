package com.empresa.clientes_api.controller;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.criarCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = Optional.ofNullable(clienteService.obterCliente(id));
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.atualizarCliente(id, clienteDTO);
        return ResponseEntity.ok(cliente);
    }

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

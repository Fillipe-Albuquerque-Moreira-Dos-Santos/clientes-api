package com.empresa.clientes_api.controller;

import com.empresa.clientes_api.dto.LogradouroDTO;
import com.empresa.clientes_api.model.Logradouro;
import com.empresa.clientes_api.repository.LogradouroRepository;
import com.empresa.clientes_api.repository.LogradouroRepositoryImpl;
import com.empresa.clientes_api.service.LogradouroService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logradouros")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class LogradouroController {

    private final LogradouroService logradouroService;
    private final LogradouroRepository logradouroRepository;

    @GetMapping("/listar")
    public List<LogradouroDTO> listarTodos() {
        List<Logradouro> logradouros = logradouroRepository.findAll();

        return logradouros.stream().map(logradouro -> {
            LogradouroDTO dto = new LogradouroDTO();
            dto.setId(logradouro.getId());
            dto.setLogradouro(logradouro.getLogradouro());
            dto.setIdCliente(logradouro.getCliente() != null ? logradouro.getCliente().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }


    @PostMapping("/salvar")
    public ResponseEntity<Logradouro> salvar(@RequestBody Logradouro logradouro) {
        return ResponseEntity.ok(logradouroService.salvar(logradouro));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Logradouro> editar(@PathVariable Long id, @RequestBody Logradouro logradouro) {
        return ResponseEntity.ok(logradouroService.editar(id, logradouro));
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        logradouroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

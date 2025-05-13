package com.empresa.clientes_api.dto;

import com.empresa.clientes_api.model.Cliente;
import com.empresa.clientes_api.model.Logradouro;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClienteDTO {

    private Long id;
    private String nome;
    private String email;
    private List<String> logradouros;
    private String logotipoBase64;
    private MultipartFile logotipo;
    private String logotipoUrl;


    // Construtor para ClienteDTO que recebe um objeto Cliente
    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.logradouros = cliente.getLogradouros().stream()
                .map(Logradouro::getLogradouro)
                .collect(Collectors.toList());
        this.logotipoUrl = "http://localhost:8080/clientes/" + cliente.getId() + "/logo";

    }



}

package com.empresa.clientes_api.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClienteDTO {

    private String nome;
    private String email;
    private List<String> logradouros;  // Lista de logradouros (endereços)
    private MultipartFile logotipo;    // Logotipo do cliente

    public ClienteDTO(String nome, String email, List<String> logradouros, MultipartFile logotipo) {
        this.nome = nome;
        this.email = email;
        this.logradouros = logradouros;
        this.logotipo = logotipo;
    }
}

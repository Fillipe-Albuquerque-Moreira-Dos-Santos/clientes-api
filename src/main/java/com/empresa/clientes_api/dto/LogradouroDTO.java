package com.empresa.clientes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogradouroDTO {

    private Long id;
    private String logradouro;
    private Long idCliente;


}

package com.empresa.clientes_api.mapper;

import com.empresa.clientes_api.dto.ClienteDTO;
import com.empresa.clientes_api.model.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDTO toClienteDTO(Cliente cliente);

    Cliente toCliente(ClienteDTO clienteDTO);
}

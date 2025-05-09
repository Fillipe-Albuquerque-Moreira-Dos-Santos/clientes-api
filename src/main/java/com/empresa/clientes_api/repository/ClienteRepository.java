package com.empresa.clientes_api.repository;

import com.empresa.clientes_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente save();
}

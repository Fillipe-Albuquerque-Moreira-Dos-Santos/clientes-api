package com.empresa.clientes_api.repository;

import com.empresa.clientes_api.model.Cliente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(@Email(message = "E-mail inválido") @NotBlank(message = "E-mail é obrigatório") String email);
    @EntityGraph(attributePaths = "logradouros")
    List<Cliente> findAll(); // agora carrega os logradouros junto
}

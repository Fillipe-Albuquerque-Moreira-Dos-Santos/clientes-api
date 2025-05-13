package com.empresa.clientes_api.repository;

import com.empresa.clientes_api.model.Logradouro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Optional;

@Repository
public interface LogradouroRepository extends JpaRepository<Logradouro, Long> {

    Optional<Logradouro> findByLogradouro(String logradouro); // ✅ CORRETO



}

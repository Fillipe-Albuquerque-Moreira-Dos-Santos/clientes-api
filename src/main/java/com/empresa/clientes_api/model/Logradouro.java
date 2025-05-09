package com.empresa.clientes_api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "logradouros")
@Data
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endereco;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Getters e Setters
}

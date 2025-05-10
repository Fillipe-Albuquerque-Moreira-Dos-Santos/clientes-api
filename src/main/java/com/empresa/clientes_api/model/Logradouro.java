package com.empresa.clientes_api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "logradouro")  // Ajustando o nome da tabela para 'logradouro'
@Data
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gerando o ID automaticamente
    @Column(name = "id_logradouro") // Coluna correspondente à tabela SQL
    private Long id;

    @Column(name = "logradouro") // Coluna de endereço
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente") // Relacionamento com Cliente
    private Cliente cliente;

    // Outros atributos e métodos
}

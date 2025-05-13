package com.empresa.clientes_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "Logradouro.excluir",
                procedureName = "sp_excluir_logradouro",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "id_logradouro", type = Long.class)
                }
        )
})

@Entity
@Table(name = "logradouro")  // Ajustando o nome da tabela para 'logradouro'
@Data
@NoArgsConstructor
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_logradouro") // Coluna correspondente à tabela SQL
    private Long id;

    @Column(name = "logradouro")
    private String logradouro;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;

}

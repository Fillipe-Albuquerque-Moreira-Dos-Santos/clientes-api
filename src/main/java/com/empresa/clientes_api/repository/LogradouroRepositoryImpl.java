package com.empresa.clientes_api.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

@Repository
public class LogradouroRepositoryImpl implements LogradouroRepositoryProcedure {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void excluirPorProcedure(Long id) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("sp_excluir_logradouro");
        query.registerStoredProcedureParameter("id_logradouro", Long.class, ParameterMode.IN);
        query.setParameter("id_logradouro", id);
        query.execute();
    }
}

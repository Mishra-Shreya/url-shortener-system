package com.urlshortener.backend.utility.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {

    @PersistenceContext
    private EntityManager entityManager;

    public Long getNextSeq(){
        String query = "SELECT nextval('uss.id_sequence')";

        Query nativeQuery = entityManager.createNativeQuery(query);
        return (Long) nativeQuery.getSingleResult();
    }
}

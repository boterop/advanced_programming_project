package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.valueobject.ID;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class DatabaseIDProvider implements IDProvider {
    private final EntityManager entityManager;

    public DatabaseIDProvider(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ID obtener() {
        Object id = entityManager.createNativeQuery("select cast(random_uuid() as varchar(36))").getSingleResult();
        return new ID(id.toString());
    }
}

package com.uq.bnb.application.usecase;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.service.RegistroApartamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: registrar un apartamento (7.3). Solo orquesta: la regla de identificación única
 * vive en RegistroApartamentoService y las validaciones del apartamento, en Apartamento.crear(...).
 */
@Service
public class CrearApartamentoUseCase {

    private final RegistroApartamentoService registroApartamentoService;

    public CrearApartamentoUseCase(RegistroApartamentoService registroApartamentoService) {
        this.registroApartamentoService = registroApartamentoService;
    }

    @Transactional
    public Apartamento ejecutar(Apartamento nuevo) {
        registroApartamentoService.registrar(nuevo);
        return nuevo;
    }
}

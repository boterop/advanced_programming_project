package com.uq.bnb.application.usecase;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.service.RegistroApartamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

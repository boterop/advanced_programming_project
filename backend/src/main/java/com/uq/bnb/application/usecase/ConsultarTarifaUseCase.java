package com.uq.bnb.application.usecase;
import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.TarifaNoEncontradaException;
import com.uq.bnb.domain.entity.Tarifa;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Service
public class ConsultarTarifaUseCase {
    private final ApartamentoRepository apartamentoRepository;
    public ConsultarTarifaUseCase(ApartamentoRepository apartamentoRepository) {
        this.apartamentoRepository = apartamentoRepository;
    }
    @Transactional(readOnly = true)
    public Tarifa ejecutar(String identificacionApartamento, UUID idTarifa) {
        return apartamentoRepository.buscarPorId(identificacionApartamento)
                .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacionApartamento))
                .tarifa(new com.uq.bnb.domain.valueobject.ID(idTarifa.toString()))
                .orElseThrow(() -> new TarifaNoEncontradaException(identificacionApartamento, idTarifa));
    }
}

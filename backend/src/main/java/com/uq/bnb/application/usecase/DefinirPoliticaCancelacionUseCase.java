package com.uq.bnb.application.usecase;
import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.valueobject.TramoAntelacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
@Service
public class DefinirPoliticaCancelacionUseCase {
    private final AlojamientoRepository alojamientoRepository;
    private final IDProvider idProvider;
    public DefinirPoliticaCancelacionUseCase(AlojamientoRepository alojamientoRepository, IDProvider idProvider) {
        this.alojamientoRepository = alojamientoRepository;
        this.idProvider = idProvider;
    }
    @Transactional
    public PoliticaCancelacion ejecutar(List<TramoAntelacion> tramos, LocalDate vigenteDesde) {
        Alojamiento alojamiento = alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
        PoliticaCancelacion nueva = alojamiento.definirNuevaPolitica(idProvider.obtener(), tramos, vigenteDesde);
        alojamientoRepository.guardar(alojamiento);
        return nueva;
    }
}

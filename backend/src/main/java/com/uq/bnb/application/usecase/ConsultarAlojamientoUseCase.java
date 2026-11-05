package com.uq.bnb.application.usecase;
import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDate;
@Service
public class ConsultarAlojamientoUseCase {
    public record Resultado(Alojamiento alojamiento, PoliticaCancelacion politicaVigente) {
    }
    private final AlojamientoRepository alojamientoRepository;
    private final Clock clock;
    public ConsultarAlojamientoUseCase(AlojamientoRepository alojamientoRepository, Clock clock) {
        this.alojamientoRepository = alojamientoRepository;
        this.clock = clock;
    }
    @Transactional(readOnly = true)
    public Resultado ejecutar() {
        Alojamiento alojamiento = alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
        return new Resultado(alojamiento, alojamiento.politicaVigente(LocalDate.now(clock)));
    }
}

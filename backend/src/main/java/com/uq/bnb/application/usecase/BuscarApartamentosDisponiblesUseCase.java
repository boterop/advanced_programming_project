package com.uq.bnb.application.usecase;
import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.service.DisponibilidadApartamentoService;
import com.uq.bnb.domain.valueobject.Estancia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class BuscarApartamentosDisponiblesUseCase {
    private final AlojamientoRepository alojamientoRepository;
    private final DisponibilidadApartamentoService disponibilidadService;
    public BuscarApartamentosDisponiblesUseCase(AlojamientoRepository alojamientoRepository,
                                                DisponibilidadApartamentoService disponibilidadService) {
        this.alojamientoRepository = alojamientoRepository;
        this.disponibilidadService = disponibilidadService;
    }
    @Transactional(readOnly = true)
    public Page<Apartamento> ejecutar(Estancia estancia, int totalOcupantes, boolean conMascota, Pageable pageable) {
        Alojamiento alojamiento = alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
        return Paginacion.paginar(
                disponibilidadService.buscarDisponibles(estancia, totalOcupantes, conMascota, alojamiento.parametros()),
                pageable);
    }
}

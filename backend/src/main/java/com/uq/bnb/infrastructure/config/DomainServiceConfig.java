package com.uq.bnb.infrastructure.config;

import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.repository.GeneradorCodigoReserva;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.service.CotizadorEstanciaService;
import com.uq.bnb.domain.service.CreacionReservaService;
import com.uq.bnb.domain.service.DisponibilidadApartamentoService;
import com.uq.bnb.domain.service.EntregaApartamentoService;
import com.uq.bnb.domain.service.GestionBloqueoService;
import com.uq.bnb.domain.service.RegistroApartamentoService;
import com.uq.bnb.domain.valueobject.DescuentoEstanciaProlongada;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.HoraLimiteNoShow;
import com.uq.bnb.domain.valueobject.HorarioAlojamiento;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import com.uq.bnb.domain.valueobject.PlazoConfirmacion;
import com.uq.bnb.domain.valueobject.PoliticaAnticipo;
import com.uq.bnb.domain.valueobject.RecargoLlegadaNocturna;
import com.uq.bnb.domain.valueobject.TiempoPreparacion;
import com.uq.bnb.domain.valueobject.UmbralEdadFacturable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Registra como beans los servicios de dominio (clases Java puras, que no conocen a Spring) y los parámetros de la
 * Ficha. Los valores salen de application.properties: cambiarlos no requiere recompilar.
 */
@Configuration
@EnableConfigurationProperties(AlojamientoProperties.class)
public class DomainServiceConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("America/Bogota"));
    }

    @Bean
    public ParametrosAlojamiento parametrosAlojamiento(AlojamientoProperties propiedades) {
        return new ParametrosAlojamiento(
                new UmbralEdadFacturable(propiedades.umbralEdadFacturable()),
                new HorarioAlojamiento(LocalTime.parse(propiedades.horaEntrada()), LocalTime.parse(propiedades.horaSalida())),
                new TiempoPreparacion(propiedades.tiempoPreparacionHoras()),
                new PlazoConfirmacion(propiedades.plazoConfirmacionHoras()),
                new HoraLimiteNoShow(LocalTime.parse(propiedades.horaLimiteNoShow())),
                new PoliticaAnticipo(propiedades.exigeAnticipo(), propiedades.porcentajeAnticipo())
        );
    }

    @Bean
    public DescuentoEstanciaProlongada descuentoEstanciaProlongada(AlojamientoProperties propiedades) {
        return new DescuentoEstanciaProlongada(propiedades.nochesDescuentoEstanciaProlongada(), propiedades.porcentajeDescuentoEstanciaProlongada());
    }

    @Bean
    public DisponibilidadApartamentoService disponibilidadApartamentoService(ApartamentoRepository apartamentoRepository,
                                                                             ReservaRepository reservaRepository) {
        return new DisponibilidadApartamentoService(apartamentoRepository, reservaRepository);
    }

    @Bean
    public CotizadorEstanciaService cotizadorEstanciaService(DescuentoEstanciaProlongada descuentoEstanciaProlongada) {
        return new CotizadorEstanciaService(descuentoEstanciaProlongada);
    }

    /** RN-Propia-02: el valor y la franja del recargo por llegada nocturna salen de la configuración. */
    @Bean
    public RecargoLlegadaNocturna recargoLlegadaNocturna(AlojamientoProperties propiedades) {
        return new RecargoLlegadaNocturna(Dinero.de(propiedades.recargoLlegadaNocturna()),
                LocalTime.parse(propiedades.horaInicioLlegadaNocturna()), LocalTime.parse(propiedades.horaFinLlegadaNocturna()));
    }

    @Bean
    public CreacionReservaService creacionReservaService(DisponibilidadApartamentoService disponibilidadService,
                                                         CotizadorEstanciaService cotizadorService,
                                                         GeneradorCodigoReserva generadorCodigo,
                                                         RecargoLlegadaNocturna recargoLlegadaNocturna) {
        return new CreacionReservaService(disponibilidadService, cotizadorService, generadorCodigo, recargoLlegadaNocturna);
    }

    @Bean
    public RegistroApartamentoService registroApartamentoService(ApartamentoRepository apartamentoRepository,
                                                                 ReservaRepository reservaRepository) {
        return new RegistroApartamentoService(apartamentoRepository, reservaRepository);
    }

    @Bean
    public GestionBloqueoService gestionBloqueoService(ReservaRepository reservaRepository) {
        return new GestionBloqueoService(reservaRepository);
    }

    @Bean
    public EntregaApartamentoService entregaApartamentoService() {
        return new EntregaApartamentoService();
    }

}

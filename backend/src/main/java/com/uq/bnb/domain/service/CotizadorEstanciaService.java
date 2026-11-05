package com.uq.bnb.domain.service;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.entity.Temporada;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.DescuentoEstanciaProlongada;
import com.uq.bnb.domain.valueobject.DetalleNoche;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.Estancia;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class CotizadorEstanciaService {
    private final DescuentoEstanciaProlongada descuentoEstanciaProlongada;
    public CotizadorEstanciaService(DescuentoEstanciaProlongada descuentoEstanciaProlongada) {
        this.descuentoEstanciaProlongada = Objects.requireNonNull(descuentoEstanciaProlongada,
                "El descuento por estancia prolongada no puede ser nulo");
    }
    public Cotizacion cotizar(Alojamiento alojamiento, Apartamento apartamento, Estancia estancia, List<Ocupante> ocupantes) {
        Objects.requireNonNull(alojamiento, "El alojamiento no puede ser nulo");
        Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
        Objects.requireNonNull(estancia, "La estancia no puede ser nula");
        Objects.requireNonNull(ocupantes, "Los ocupantes no pueden ser nulos");
        int umbralEdad = alojamiento.parametros().umbralEdadFacturable().anios();
        int facturables = (int) ocupantes.stream()
                .filter(o -> o.esFacturable(estancia.entrada(), umbralEdad))
                .count();
        if (facturables == 0) {
            throw new ReglaDominioException("La estancia debe tener al menos un ocupante facturable (con "
                    + umbralEdad + " años o más a la fecha de entrada)");
        }
        boolean conDescuento = descuentoEstanciaProlongada.aplicaPara(estancia.noches());
        List<DetalleNoche> desglose = new ArrayList<>();
        for (LocalDate noche : estancia.nochesOcupadas()) {
            Temporada temporada = alojamiento.temporadaVigente(noche);
            Dinero tarifa = apartamento.tarifaVigente(temporada.id(), noche).valorPorNoche();
            if (conDescuento) {
                tarifa = descuentoEstanciaProlongada.aplicarA(tarifa);
            }
            desglose.add(new DetalleNoche(noche, temporada.nombre(), tarifa, facturables));
        }
        return new Cotizacion(desglose);
    }
}

package com.project.advanced.project.infrastructure.persistence.jpa.entity;

import com.project.advanced.project.domain.valueobject.CanalOrigen;
import com.project.advanced.project.domain.valueobject.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = Generationtype.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 15)
    private String codigo;

    @Column(name = "apartamento_id"), nullable = false, length = 15)
    private String apartamentoId;

    @Embedded //Este elemento se vuelve columnas de la tabla de reserva
    private EstanciaEmbedable estancia;

    @Enumerated(Enum.type.STRING)
    @Column(name = "estado", nullable = false, length = 12)
    private EstadoReserva estado;

    @Enumerated(EnumTyoe.STRING)
    @Column(name = "canal_origen",nullable = false, length 12)
    private CanalOrigen canalOrigen;

    @Column(name = "documento_tirular", nullable = false, length = 15)
    private String documentoTitular;

    @CollectionTable(name = "reserva_ocupantes", //creacion de la tabla
                    joinColumns = @JoinColumn(name = "reserva_id")) //Relacion llave foranea
    private List<OcupanteEmbeddable> ocupantes = new ArrayList<>();

    @Column(name = "hora_estimada_llegada")
    private LocalTime horaEstimadaLLegada;

    @Column(name = "valor_congelado", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorCongelado;

    @Embedded
    private PoliticaEmbeddable politicaCongelada;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "motivo_cancelacion", length = 300)
    private String motivoCancelacion;
}

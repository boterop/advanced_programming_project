package com.uq.bnb.infrastructure.config;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.math.BigDecimal;
@Validated
@ConfigurationProperties(prefix = "sga.alojamiento")
public record AlojamientoProperties(
        @NotNull @Min(0) Integer umbralEdadFacturable,
        @NotBlank @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "debe tener formato HH:mm") String horaEntrada,
        @NotBlank @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "debe tener formato HH:mm") String horaSalida,
        @NotNull @Min(0) Integer tiempoPreparacionHoras,
        @NotNull @Min(0) Integer plazoConfirmacionHoras,
        @NotBlank @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "debe tener formato HH:mm") String horaLimiteNoShow,
        @NotNull Boolean exigeAnticipo,
        @NotNull BigDecimal porcentajeAnticipo,
        @NotNull @Min(1) Integer nochesDescuentoEstanciaProlongada,
        @NotNull BigDecimal porcentajeDescuentoEstanciaProlongada,
        @NotNull @Positive Long recargoLlegadaNocturna,
        @NotBlank @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "debe tener formato HH:mm") String horaInicioLlegadaNocturna,
        @NotBlank @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "debe tener formato HH:mm") String horaFinLlegadaNocturna) {
}

package com.uq.bnb.application.dto.request;

import com.uq.bnb.domain.valueobject.EstadoOperativo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CrearApartamentoRequest(
    @NotBlank(message = "La identificación del apartamento es obligatoria") String identificacion,
    @NotBlank(message = "El nombre del apartamento es obligatorio") String nombre,
    @Min(value = 1, message = "La capacidad debe ser al menos 1") int capacidad,
    @NotEmpty(message = "El apartamento debe tener al menos un dormitorio") @Valid
        List<DormitorioRequest> dormitorios,
    @NotNull(message = "El estado operativo inicial es obligatorio")
        EstadoOperativo estadoOperativoInicial,
    Boolean permiteMascotas) {}

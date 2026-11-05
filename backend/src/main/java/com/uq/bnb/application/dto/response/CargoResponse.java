package com.uq.bnb.application.dto.response;
import com.uq.bnb.domain.valueobject.TipoCargo;
import java.util.UUID;
public record CargoResponse(UUID id, TipoCargo tipo, long valor) {
}

package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.CanalOrigen;

import java.nio.charset.StandardCharsets;

@Getter
public final class CanalVenta {

    private final ID id;
    private final String identificacion;
    private final String nombre;
    private final CanalOrigen origen;
    private final boolean activo;

    private CanalVenta(ID id, String identificacion, String nombre, CanalOrigen origen, boolean activo) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.origen = origen;
        this.activo = activo;
    }

    public static CanalVenta crear(ID id, String identificacion, String nombre, CanalOrigen origen, boolean activo) {
        if (identificacion == null) {
            throw new ReglaDominioException("El canal de venta debe tener una identificacion");
        }

        if (nombre == null || nombre.isBlank()){
            throw new ReglaDominioException("El canal de venta debe tener un nombre");

        }

        if (origen == null){
            throw new ReglaDominioException("El canal de venta debe tener un tipo de origen");

        }
        return new CanalVenta(id, identificacion, nombre, origen, activo);
    }

    public void verificarCanalOrigen() {
        if (!activo) {
            throw new ReglaDominioException("El canal de venta no esta activo y debe seleccionar otro canal origen");
        }
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof CanalVenta entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}

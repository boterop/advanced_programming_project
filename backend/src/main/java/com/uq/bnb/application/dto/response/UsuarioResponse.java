package com.uq.bnb.application.dto.response;

import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.application.security.RolUsuario;

public record UsuarioResponse(
        String id,
        String nombre,
        String correo,
        String telefono,
        String documento,
        RolUsuario rol,
        boolean activo
) {
    public static UsuarioResponse de(CuentaUsuario cuenta) {
        return new UsuarioResponse(
                cuenta.id().valor(),
                cuenta.nombre(),
                cuenta.correo(),
                cuenta.telefono(),
                cuenta.documento(),
                cuenta.rol(),
                cuenta.activo());
    }
}

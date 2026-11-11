package com.uq.bnb.application.port;

import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.domain.valueobject.ID;
import java.util.Optional;

// Puerto de salida de las cuentas de acceso. El correo se recibe ya normalizado en minúsculas.
public interface UsuarioRepository {
    Optional<CuentaUsuario> buscarPorId(ID id);

    Optional<CuentaUsuario> buscarPorCorreo(String correo);

    boolean existePorCorreo(String correo);

    boolean existePorDocumento(String documento);

    void guardar(CuentaUsuario cuenta);
}

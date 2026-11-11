package com.uq.bnb.fixtures;

import com.uq.bnb.application.port.UsuarioRepository;
import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.domain.valueobject.ID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUsuarioRepository implements UsuarioRepository {
    private final Map<ID, CuentaUsuario> cuentas = new HashMap<>();

    @Override
    public Optional<CuentaUsuario> buscarPorId(ID id) {
        return Optional.ofNullable(cuentas.get(id));
    }

    @Override
    public Optional<CuentaUsuario> buscarPorCorreo(String correo) {
        return cuentas.values().stream().filter(cuenta -> cuenta.correo().equals(correo)).findFirst();
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return buscarPorCorreo(correo).isPresent();
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return cuentas.values().stream().anyMatch(cuenta -> cuenta.documento().equals(documento));
    }

    @Override
    public void guardar(CuentaUsuario cuenta) {
        cuentas.put(cuenta.id(), cuenta);
    }

    public int total() {
        return cuentas.size();
    }
}

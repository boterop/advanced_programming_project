package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.UsuarioDuplicadoException;
import com.uq.bnb.application.port.CodificadorContrasena;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.application.port.UsuarioRepository;
import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.application.security.PoliticaContrasena;
import com.uq.bnb.application.security.RolUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class RegistrarUsuarioHuespedUseCase {
    private final UsuarioRepository usuarioRepository;
    private final CodificadorContrasena codificadorContrasena;
    private final IDProvider idProvider;

    public RegistrarUsuarioHuespedUseCase(
            UsuarioRepository usuarioRepository,
            CodificadorContrasena codificadorContrasena,
            IDProvider idProvider) {
        this.usuarioRepository = usuarioRepository;
        this.codificadorContrasena = codificadorContrasena;
        this.idProvider = idProvider;
    }

    // El registro público siempre crea un HUESPED.
    @Transactional
    public CuentaUsuario ejecutar(
            String nombre,
            String correo,
            String contrasena,
            String telefono,
            String documento,
            LocalDate fechaNacimiento) {
        PoliticaContrasena.validar(contrasena);
        if (usuarioRepository.existePorCorreo(CuentaUsuario.normalizarCorreo(correo))) {
            throw new UsuarioDuplicadoException("correo electrónico");
        }
        if (usuarioRepository.existePorDocumento(CuentaUsuario.normalizarDocumento(documento))) {
            throw new UsuarioDuplicadoException("documento de identidad");
        }
        CuentaUsuario nueva =
                CuentaUsuario.crear(
                        idProvider.obtener(),
                        nombre,
                        correo,
                        codificadorContrasena.codificar(contrasena),
                        telefono,
                        documento,
                        fechaNacimiento,
                        RolUsuario.HUESPED);
        usuarioRepository.guardar(nueva);
        return nueva;
    }
}

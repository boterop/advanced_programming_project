package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.UsuarioNoEncontradoException;
import com.uq.bnb.application.port.UsuarioRepository;
import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.ID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

// Baja lógica de un usuario: la cuenta se conserva, inactiva, para mantener la trazabilidad.
// No toca al titular ni a las reservas históricas.
@Service
public class DesactivarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    public DesactivarUsuarioUseCase(
            UsuarioRepository usuarioRepository, ReservaRepository reservaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public CuentaUsuario ejecutar(UUID usuarioId) {
        Objects.requireNonNull(usuarioId, "El identificador del usuario es obligatorio");
        CuentaUsuario cuenta =
                usuarioRepository
                        .buscarPorId(new ID(usuarioId.toString()))
                        .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        CuentaUsuario inactiva = cuenta.desactivar();

        int reservasActivas = reservaRepository.buscarActivasPorTitular(cuenta.documento()).size();
        if (reservasActivas > 0) {
            throw new ReglaDominioException(
                    "No se puede desactivar el usuario: es titular de "
                            + reservasActivas
                            + " reserva(s) activa(s)");
        }

        usuarioRepository.guardar(inactiva);
        return inactiva;
    }
}

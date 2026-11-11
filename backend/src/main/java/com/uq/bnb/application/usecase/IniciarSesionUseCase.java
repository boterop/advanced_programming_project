package com.uq.bnb.application.usecase;

import com.uq.bnb.application.dto.response.TokenResponse;
import com.uq.bnb.application.exception.CredencialesInvalidasException;
import com.uq.bnb.application.port.CodificadorContrasena;
import com.uq.bnb.application.port.EmisorToken;
import com.uq.bnb.application.port.UsuarioRepository;
import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.application.security.TokenEmitido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IniciarSesionUseCase {
    private static final String TIPO_TOKEN = "Bearer";

    // Hash BCrypt válido de una clave que nadie conoce. Se compara cuando el correo no existe para
    // igualar el costo de la comparación. El tiempo de respuesta no delata qué correos existen.
    private static final String HASH_DE_RELLENO =
            "{bcrypt}$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    private final UsuarioRepository usuarioRepository;
    private final CodificadorContrasena codificadorContrasena;
    private final EmisorToken emisorToken;

    public IniciarSesionUseCase(
            UsuarioRepository usuarioRepository,
            CodificadorContrasena codificadorContrasena,
            EmisorToken emisorToken) {
        this.usuarioRepository = usuarioRepository;
        this.codificadorContrasena = codificadorContrasena;
        this.emisorToken = emisorToken;
    }

    @Transactional(readOnly = true)
    public TokenResponse ejecutar(String correo, String contrasena) {
        Objects.requireNonNull(correo, "El correo es obligatorio");
        Objects.requireNonNull(contrasena, "La contraseña es obligatoria");

        Optional<CuentaUsuario> cuenta =
                usuarioRepository.buscarPorCorreo(CuentaUsuario.normalizarCorreo(correo));
        boolean coincide =
                codificadorContrasena.coincide(
                        contrasena, cuenta.map(CuentaUsuario::contrasenaCifrada).orElse(HASH_DE_RELLENO));

        // Un solo error para correo inexistente, contraseña errada y usuario inactivo.
        if (cuenta.isEmpty() || !coincide || !cuenta.get().activo()) {
            throw new CredencialesInvalidasException();
        }

        CuentaUsuario usuario = cuenta.get();
        List<String> roles = List.of(usuario.rol().name());
        TokenEmitido token = emisorToken.emitir(usuario.correo(), usuario.documento(), roles);
        return new TokenResponse(token.token(), TIPO_TOKEN, token.expiraEn(), roles);
    }
}

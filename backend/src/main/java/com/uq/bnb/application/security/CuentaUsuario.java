package com.uq.bnb.application.security;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record CuentaUsuario(
        ID id,
        String nombre,
        String correo,
        String contrasenaCifrada,
        String telefono,
        String documento,
        LocalDate fechaNacimiento,
        RolUsuario rol,
        boolean activo
) {
    private static final Pattern CORREO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern TELEFONO = Pattern.compile("^\\+?[0-9]{7,15}$");
    private static final int MAXIMO_NOMBRE = 100;
    private static final int MAXIMO_CORREO = 120;
    private static final int MAXIMO_DOCUMENTO = 15;

    public CuentaUsuario {
        Objects.requireNonNull(id, "El identificador del usuario es obligatorio");
        nombre = textoObligatorio(nombre, "El nombre es obligatorio");
        correo = normalizarCorreo(correo);
        contrasenaCifrada =
                textoObligatorio(contrasenaCifrada, "La contraseña cifrada es obligatoria");
        telefono = textoObligatorio(telefono, "El teléfono es obligatorio");
        documento = normalizarDocumento(documento);
        Objects.requireNonNull(fechaNacimiento, "La fecha de nacimiento es obligatoria");
        Objects.requireNonNull(rol, "El rol es obligatorio");
    }

    // Alta de un usuario nuevo: valida el formato de los datos y nace activo.
    public static CuentaUsuario crear(
            ID id,
            String nombre,
            String correo,
            String contrasenaCifrada,
            String telefono,
            String documento,
            LocalDate fechaNacimiento,
            RolUsuario rol) {
        String correoNormalizado = normalizarCorreo(correo);
        if (!CORREO.matcher(correoNormalizado).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }
        String telefonoNormalizado =
                textoObligatorio(telefono, "El teléfono es obligatorio").replace(" ", "").replace("-", "");
        if (!TELEFONO.matcher(telefonoNormalizado).matches()) {
            throw new IllegalArgumentException("El teléfono no tiene un formato válido");
        }
        Objects.requireNonNull(fechaNacimiento, "La fecha de nacimiento es obligatoria");
        if (!fechaNacimiento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento debe ser anterior a hoy");
        }
        limitar(textoObligatorio(nombre, "El nombre es obligatorio"), MAXIMO_NOMBRE, "El nombre");
        limitar(correoNormalizado, MAXIMO_CORREO, "El correo");
        limitar(normalizarDocumento(documento), MAXIMO_DOCUMENTO, "El documento de identidad");
        return new CuentaUsuario(
                id,
                nombre,
                correoNormalizado,
                contrasenaCifrada,
                telefonoNormalizado,
                documento,
                fechaNacimiento,
                rol,
                true);
    }

    // El correo se guarda y se compara en minúsculas y sin espacios.
    public static String normalizarCorreo(String correo) {
        return textoObligatorio(correo, "El correo es obligatorio").toLowerCase(Locale.ROOT);
    }

    public static String normalizarDocumento(String documento) {
        return textoObligatorio(documento, "El documento de identidad es obligatorio");
    }

    // Baja lógica (PROJECT.md 7.2): la cuenta se conserva, pero ya no puede iniciar sesión.
    public CuentaUsuario desactivar() {
        if (!activo) {
            throw new ReglaDominioException("El usuario ya está inactivo");
        }
        return new CuentaUsuario(
                id, nombre, correo, contrasenaCifrada, telefono, documento, fechaNacimiento, rol, false);
    }

    private static String textoObligatorio(String valor, String mensaje) {
        Objects.requireNonNull(valor, mensaje);
        if (valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    private static void limitar(String valor, int maximo, String campo) {
        if (valor.length() > maximo) {
            throw new IllegalArgumentException(campo + " no puede superar los " + maximo + " caracteres");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CuentaUsuario cuenta && id.equals(cuenta.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // No incluye la contraseña cifrada: no debe llegar a los logs.
    @Override
    public String toString() {
        return "CuentaUsuario[id="
                + id.valor()
                + ", correo="
                + correo
                + ", rol="
                + rol
                + ", activo="
                + activo
                + "]";
    }
}

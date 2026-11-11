package com.uq.bnb.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        String correo,
        @NotBlank(message = "La contraseña es obligatoria") String contrasena
) {
    // La contraseña no debe llegar a los logs.
    @Override
    public String toString() {
        return "LoginRequest[correo=" + correo + ", contrasena=***]";
    }
}

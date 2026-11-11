package com.uq.bnb.application.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

// Registro público que no lleva rol, el registro siempre crea un huésped.
public record RegistroRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 120, message = "El correo no puede superar los 120 caracteres")
        String correo,
        @NotBlank(message = "La contraseña es obligatoria")
        @Pattern(
                regexp = "^(?=.*\\p{Lu})(?=.*\\d).{8,72}$",
                message = "La contraseña debe tener al menos 8 caracteres, con mayúsculas y números")
        String contrasena,
        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
        String telefono,
        @NotBlank(message = "El documento de identidad es obligatorio")
        @Size(max = 15, message = "El documento no puede superar los 15 caracteres")
        String documento,
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
        LocalDate fechaNacimiento
) {
    // La contraseña no debe llegar a los logs.
    @Override
    public String toString() {
        return "RegistroRequest[nombre=" + nombre + ", correo=" + correo + ", contrasena=***]";
    }
}

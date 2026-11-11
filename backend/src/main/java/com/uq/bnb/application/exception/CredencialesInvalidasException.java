package com.uq.bnb.application.exception;

// Mensaje único a propósito: no revela si falló el correo, la contraseña o el estado del usuario.
public class CredencialesInvalidasException extends RuntimeException{
    public CredencialesInvalidasException() {
        super("Credenciales inválidas");
    }
}

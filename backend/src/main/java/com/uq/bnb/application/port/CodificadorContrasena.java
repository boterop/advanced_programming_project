package com.uq.bnb.application.port;

// Puerto de salida para cifrar y comparar contraseñas; la implementación BCrypt es externa.
public interface CodificadorContrasena {
    String codificar(String contrasena);

    boolean coincide(String contrasena, String cifrada);
}

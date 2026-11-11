package com.uq.bnb.fixtures;

import com.uq.bnb.application.port.CodificadorContrasena;

// Cifrado reversible solo para pruebas: "cifrada:" + contraseña.
public class FakeCodificadorContrasena implements CodificadorContrasena {
    private int comparaciones;

    @Override
    public String codificar(String contrasena) {
        return "cifrada:" + contrasena;
    }

    @Override
    public boolean coincide(String contrasena, String cifrada) {
        comparaciones++;
        return cifrada.equals("cifrada:" + contrasena);
    }

    public int comparaciones() {
        return comparaciones;
    }
}

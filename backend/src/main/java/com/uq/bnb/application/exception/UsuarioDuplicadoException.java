package com.uq.bnb.application.exception;

public class UsuarioDuplicadoException extends RuntimeException{
    public UsuarioDuplicadoException(String campo) {
        super("Ya existe un usuario registrado con ese " + campo);
    }
}

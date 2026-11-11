package com.uq.bnb.application.port;

import com.uq.bnb.application.security.TokenEmitido;
import java.util.List;

// Puerto de salida que emite el token de sesión firmado; la implementación JWT es externa.
public interface EmisorToken {
    TokenEmitido emitir(String correo, String documento, List<String> roles);
}

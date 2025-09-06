package com.playlist_backend.exceptions;

public class ExcepcionNegocio extends RuntimeException {
    public ExcepcionNegocio(String mensaje) {
        super(mensaje);
    }
}

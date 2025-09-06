package com.playlist_backend.excepciones;

public class ExcepcionTecnica extends RuntimeException {
    public ExcepcionTecnica(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

package com.playlist_backend.error;

import com.playlist_backend.excepciones.ExcepcionNegocio;
import com.playlist_backend.excepciones.ExcepcionTecnica;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
public class ManejadorErrores extends ResponseEntityExceptionHandler {

    private static final String OCURRIO_UN_ERROR_FAVOR_CONTACTAR_AL_ADMINISTRADOR = "Ocurrio un error favor contactar al administrador.";

    private static final ConcurrentHashMap<String, Integer> CODE_STATUS = new ConcurrentHashMap<>();

    public ManejadorErrores() {
        CODE_STATUS.put(ExcepcionNegocio.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
        CODE_STATUS.put(ExcepcionTecnica.class.getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Error> manejadorExcepciones(Exception exception) {
        ResponseEntity<Error> resultado;

        String nombreExcepcion = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        Integer code = CODE_STATUS.get(nombreExcepcion);

        if (code != null) {
            Error error = new Error(nombreExcepcion, message);
            resultado = new ResponseEntity<>(error, HttpStatus.valueOf(code));
        } else {
            Error error = new Error(nombreExcepcion, OCURRIO_UN_ERROR_FAVOR_CONTACTAR_AL_ADMINISTRADOR);
            resultado = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resultado;
    }
}

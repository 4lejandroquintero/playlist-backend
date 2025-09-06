package com.playlist_backend.error;

import com.playlist_backend.excepciones.ExcepcionNegocio;
import com.playlist_backend.excepciones.ExcepcionTecnica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ManejadorErroresTest {

    private ManejadorErrores manejadorErrores;

    @BeforeEach
    void setUp() {
        manejadorErrores = new ManejadorErrores();
    }

    @Test
    void manejarExcepcionNegocio_devuelveBadRequest() {
        // Arrange
        ExcepcionNegocio ex = new ExcepcionNegocio("Error de negocio");

        // Act
        ResponseEntity<Error> response = manejadorErrores.manejadorExcepciones(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ExcepcionNegocio", response.getBody().mensaje());
        assertEquals("Error de negocio", response.getBody().nombreExcepcion());
    }

    @Test
    void manejarExcepcionTecnica_devuelveInternalServerError() {
        // Arrange
        ExcepcionTecnica ex = new ExcepcionTecnica("Error técnico", new RuntimeException("Causa"));

        // Act
        ResponseEntity<Error> response = manejadorErrores.manejadorExcepciones(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ExcepcionTecnica", response.getBody().mensaje());
        assertEquals("Error técnico", response.getBody().nombreExcepcion());
    }

    @Test
    void manejarExcepcionDesconocida_devuelveMensajeGenerico() {
        // Arrange
        Exception ex = new NullPointerException("Algo salió mal");

        // Act
        ResponseEntity<Error> response = manejadorErrores.manejadorExcepciones(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NullPointerException", response.getBody().mensaje());
        assertEquals("Ocurrio un error favor contactar al administrador.", response.getBody().nombreExcepcion());
    }
}

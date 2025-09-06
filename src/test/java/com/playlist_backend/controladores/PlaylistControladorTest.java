package com.playlist_backend.controladores;

import com.playlist_backend.modelos.Playlist;
import com.playlist_backend.modelos.Cancion;
import com.playlist_backend.servicios.ServicioPlaylist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistControladorTest {

    @Mock
    private ServicioPlaylist service;

    @InjectMocks
    private PlaylistControlador controlador;

    private Playlist playlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlist = new Playlist();
        playlist.setNombre("MiPlaylist");
    }

    @Test
    void testAddSongToPlaylist() {
        Cancion cancion = new Cancion();
        cancion.setTitulo("MiCancion");
        when(service.addSongToPlaylist(eq("MiPlaylist"), any(Cancion.class))).thenReturn(playlist);

        ResponseEntity<Playlist> response = controlador.addSongToPlaylist("MiPlaylist", cancion);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playlist, response.getBody());
        verify(service, times(1)).addSongToPlaylist("MiPlaylist", cancion);
    }

    @Test
    void testGetAll() {
        when(service.findAll()).thenReturn(List.of(playlist));

        List<Playlist> result = controlador.getAll();

        assertEquals(1, result.size());
        assertEquals(playlist, result.get(0));
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetOne() {
        when(service.getByName("MiPlaylist")).thenReturn(playlist);

        ResponseEntity<Playlist> response = controlador.getOne("MiPlaylist");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playlist, response.getBody());
        verify(service, times(1)).getByName("MiPlaylist");
    }

    @Test
    void testDelete() {
        ResponseEntity<Void> response = controlador.delete("MiPlaylist");

        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).deleteByName("MiPlaylist");
    }
}

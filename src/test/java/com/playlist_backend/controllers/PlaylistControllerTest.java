package com.playlist_backend.controllers;

import com.playlist_backend.models.Playlist;
import com.playlist_backend.models.Song;
import com.playlist_backend.services.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

class PlaylistControllerTest {

    @Mock
    private PlaylistService service;

    @InjectMocks
    private PlaylistController controller;

    private Playlist playlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlist = new Playlist();
        playlist.setNombre("Rock Classics");
        playlist.setCanciones(new ArrayList<>());
    }

    @Test
    void create_ShouldReturnBadRequest_WhenNameIsNull() {
        Playlist invalid = new Playlist();
        invalid.setNombre("  ");

        ResponseEntity<?> response = controller.create(invalid);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("El nombre de la lista no puede ser nulo o vacío");
        verifyNoInteractions(service);
    }

    @Test
    void create_ShouldReturnConflict_WhenPlaylistAlreadyExists() {
        when(service.exists("Rock Classics")).thenReturn(true);

        ResponseEntity<?> response = controller.create(playlist);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo("La lista ya existe");
        verify(service).exists("Rock Classics");
        verifyNoMoreInteractions(service);
    }

    @Test
    void addSongToPlaylist_ShouldReturnOk_WhenPlaylistExists() {
        Song song = new Song();
        song.setTitulo("Bohemian Rhapsody");

        when(service.findByName("Rock Classics")).thenReturn(Optional.of(playlist));
        when(service.save(any())).thenReturn(playlist);

        ResponseEntity<Playlist> response = controller.addSongToPlaylist("Rock Classics", song);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCanciones()).contains(song);

        ArgumentCaptor<Playlist> captor = ArgumentCaptor.forClass(Playlist.class);
        verify(service).save(captor.capture());
        assertThat(captor.getValue().getCanciones()).contains(song);
    }

    @Test
    void addSongToPlaylist_ShouldReturnNotFound_WhenPlaylistDoesNotExist() {
        Song song = new Song();
        when(service.findByName("Unknown")).thenReturn(Optional.empty());

        ResponseEntity<Playlist> response = controller.addSongToPlaylist("Unknown", song);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(service).findByName("Unknown");
    }

    @Test
    void getAll_ShouldReturnAllPlaylists() {
        List<Playlist> playlists = List.of(playlist);
        when(service.findAll()).thenReturn(playlists);

        List<Playlist> result = controller.getAll();

        assertThat(result).hasSize(1);
        assertThat(result).contains(playlist);
        verify(service).findAll();
    }

    @Test
    void getOne_ShouldReturnPlaylist_WhenExists() {
        when(service.findByName("Rock Classics")).thenReturn(Optional.of(playlist));

        ResponseEntity<?> response = controller.getOne("Rock Classics");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(playlist);
    }

    @Test
    void getOne_ShouldReturnNotFound_WhenNotExists() {
        when(service.findByName("Unknown")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getOne("Unknown");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Lista no encontrada");
    }

    @Test
    void search_ShouldReturnPlaylistsContainingName() {
        when(service.findByNameContaining("Rock")).thenReturn(List.of(playlist));

        List<Playlist> result = controller.search("Rock");

        assertThat(result).contains(playlist);
        verify(service).findByNameContaining("Rock");
    }

    @Test
    void delete_ShouldReturnNoContent_WhenPlaylistExists() {
        when(service.exists("Rock Classics")).thenReturn(true);

        ResponseEntity<?> response = controller.delete("Rock Classics");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(service).deleteByName("Rock Classics");
    }

    @Test
    void delete_ShouldReturnNotFound_WhenPlaylistNotExists() {
        when(service.exists("Unknown")).thenReturn(false);

        ResponseEntity<?> response = controller.delete("Unknown");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Lista no encontrada");
        verify(service).exists("Unknown");
    }
}
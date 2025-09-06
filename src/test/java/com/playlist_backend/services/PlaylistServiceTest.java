package com.playlist_backend.services;

import static org.junit.jupiter.api.Assertions.*;

import com.playlist_backend.exceptions.ExcepcionNegocio;
import com.playlist_backend.exceptions.ExcepcionTecnica;
import com.playlist_backend.models.Playlist;
import com.playlist_backend.models.Song;
import com.playlist_backend.repositories.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PlaylistServiceTest {

    @Mock
    private PlaylistRepository repository;

    @InjectMocks
    private PlaylistService service;

    private Playlist playlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlist = new Playlist("Rock", "Clásicos del rock");
    }

    @Test
    void save_ShouldCallRepositorySave() {
        when(repository.save(playlist)).thenReturn(playlist);

        Playlist result = service.save(playlist);

        assertThat(result).isEqualTo(playlist);
        verify(repository).save(playlist);
    }

    @Test
    void findAll_ShouldReturnAllPlaylists() {
        when(repository.findAll()).thenReturn(List.of(playlist));

        List<Playlist> result = service.findAll();

        assertThat(result).hasSize(1).contains(playlist);
        verify(repository).findAll();
    }

    @Test
    void findByName_ShouldReturnOptionalPlaylist() {
        when(repository.findById("Rock")).thenReturn(Optional.of(playlist));

        Optional<Playlist> result = service.findByName("Rock");

        assertThat(result).isPresent().contains(playlist);
        verify(repository).findById("Rock");
    }

    @Test
    void deleteByName_ShouldCallRepositoryDeleteById() {
        when(repository.existsById("Rock")).thenReturn(true);

        service.deleteByName("Rock");

        verify(repository).deleteById("Rock");
    }

    @Test
    void exists_ShouldReturnTrueWhenRepositoryReturnsTrue() {
        when(repository.existsById("Rock")).thenReturn(true);

        boolean exists = service.exists("Rock");

        assertThat(exists).isTrue();
        verify(repository).existsById("Rock");
    }

    @Test
    void exists_ShouldReturnFalseWhenRepositoryReturnsFalse() {
        when(repository.existsById("Pop")).thenReturn(false);

        boolean exists = service.exists("Pop");

        assertThat(exists).isFalse();
        verify(repository).existsById("Pop");
    }

    @Test
    void findByNameContaining_ShouldReturnFilteredPlaylists() {
        when(repository.findByNombreContainingIgnoreCase("rock"))
                .thenReturn(List.of(playlist));

        List<Playlist> result = service.findByNameContaining("rock");

        assertThat(result).contains(playlist);
        verify(repository).findByNombreContainingIgnoreCase("rock");
    }

    @Test
    void savePlaylist_WithSongs_ShouldSetPlaylistReference() {
        Playlist playlist = new Playlist("rock", "clasicos");

        Song song = new Song();
        song.setTitulo("Highway to Hell");

        playlist.setCanciones(List.of(song));

        when(repository.save(playlist)).thenReturn(playlist);

        Playlist saved = service.save(playlist);

        assertEquals("rock", saved.getNombre());
        assertEquals(1, saved.getCanciones().size());
        assertEquals(saved, saved.getCanciones().get(0).getPlaylist());
        verify(repository).save(playlist);
    }

    @Test
    void equalsAndHashcode_ShouldWork() {
        Playlist p1 = new Playlist("pop", "desc1");
        Playlist p2 = new Playlist("pop", "desc2");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        Playlist p3 = new Playlist("rock", "otra");
        assertNotEquals(p1, p3);
    }

    @Test
    void findByName_ShouldReturnPlaylist() {
        Playlist playlist = new Playlist("salsa", "movida");
        when(repository.findById("salsa")).thenReturn(Optional.of(playlist));

        Optional<Playlist> result = service.findByName("salsa");

        assertTrue(result.isPresent());
        assertEquals("salsa", result.get().getNombre());
    }

    @Test
    void save_ShouldThrowExcepcionNegocio_WhenNameIsNull() {
        Playlist invalid = new Playlist(null, "desc");
        ExcepcionNegocio ex = assertThrows(ExcepcionNegocio.class, () -> service.save(invalid));
        assertThat(ex.getMessage()).contains("no puede ser nulo");
    }

    @Test
    void save_ShouldThrowExcepcionNegocio_WhenNameHasInvalidChars() {
        Playlist invalid = new Playlist("Rock!!", "desc");
        ExcepcionNegocio ex = assertThrows(ExcepcionNegocio.class, () -> service.save(invalid));
        assertThat(ex.getMessage()).contains("caracteres inválidos");
    }

    @Test
    void save_ShouldThrowExcepcionNegocio_WhenPlaylistExists() {
        Playlist existing = new Playlist("Rock", "desc");
        when(repository.existsById("Rock")).thenReturn(true);

        ExcepcionNegocio ex = assertThrows(ExcepcionNegocio.class, () -> service.save(existing));
        assertThat(ex.getMessage()).contains("ya existe");
    }

    @Test
    void save_ShouldThrowExcepcionTecnica_WhenRepositoryFails() {
        when(repository.existsById(any())).thenReturn(false);
        when(repository.save(any())).thenThrow(new RuntimeException("DB error"));

        Playlist p = new Playlist("Jazz", "desc");
        ExcepcionTecnica ex = assertThrows(ExcepcionTecnica.class, () -> service.save(p));
        assertThat(ex.getMessage()).contains("Error al guardar");
    }

    @Test
    void addSongToPlaylist_ShouldThrowExcepcionNegocio_WhenListNotFound() {
        when(repository.findById("NoExiste")).thenReturn(Optional.empty());
        Song song = new Song();
        song.setTitulo("Test");

        ExcepcionNegocio ex = assertThrows(ExcepcionNegocio.class, () ->
                service.addSongToPlaylist("NoExiste", song));
        assertThat(ex.getMessage()).contains("Lista no encontrada");
    }

    @Test
    void addSongToPlaylist_ShouldThrowExcepcionNegocio_WhenSongAlreadyExists() {
        Song existing = new Song();
        existing.setTitulo("Highway to Hell");
        playlist.setCanciones(List.of(existing));
        when(repository.findById("Rock")).thenReturn(Optional.of(playlist));

        Song duplicate = new Song();
        duplicate.setTitulo("Highway to Hell");

        ExcepcionNegocio ex = assertThrows(ExcepcionNegocio.class, () ->
                service.addSongToPlaylist("Rock", duplicate));
        assertThat(ex.getMessage()).contains("ya está en la lista");
    }

    @Test
    void deleteByName_ShouldThrowExcepcionNegocio_WhenListNotFound() {
        when(repository.existsById("NoExiste")).thenReturn(false);

        ExcepcionNegocio ex = assertThrows(ExcepcionNegocio.class, () -> service.deleteByName("NoExiste"));
        assertThat(ex.getMessage()).contains("Lista no encontrada");
    }

    @Test
    void addSongToPlaylist_ShouldThrowExcepcionTecnica_WhenRepositoryFails() {
        Song song = new Song();
        song.setTitulo("New Song");
        when(repository.findById("Rock")).thenReturn(Optional.of(playlist));
        doThrow(new RuntimeException("DB fail")).when(repository).save(any());

        ExcepcionTecnica ex = assertThrows(ExcepcionTecnica.class,
                () -> service.addSongToPlaylist("Rock", song));

        assertThat(ex.getMessage()).contains("Error al agregar");
    }

    @Test
    void getByName_ShouldThrowExcepcionNegocio_WhenPlaylistNotFound() {
        when(repository.findById("NoExiste")).thenReturn(Optional.empty());

        ExcepcionNegocio ex = assertThrows(ExcepcionNegocio.class,
                () -> service.getByName("NoExiste"));

        assertThat(ex.getMessage()).contains("Lista no encontrada");
    }

    @Test
    void deleteByName_ShouldThrowExcepcionTecnica_WhenRepositoryFails() {
        when(repository.existsById("Rock")).thenReturn(true);
        doThrow(new RuntimeException("DB fail")).when(repository).deleteById("Rock");

        ExcepcionTecnica ex = assertThrows(ExcepcionTecnica.class,
                () -> service.deleteByName("Rock"));

        assertThat(ex.getMessage()).contains("Error al eliminar");
    }

    @Test
    void addSongToPlaylist_ShouldAddSong() {
        PlaylistRepository repository = mock(PlaylistRepository.class);
        PlaylistService service = new PlaylistService(repository);

        Playlist playlist = new Playlist("Pop", "Lista pop 2025");
        playlist.setCanciones(new ArrayList<>());

        when(repository.findById("Pop")).thenReturn(Optional.of(playlist));
        when(repository.save(any(Playlist.class))).thenAnswer(i -> i.getArgument(0));

        Song song = new Song("Levitating", "Dua Lipa", "Future Nostalgia", "2020", "Pop");

        Playlist updated = service.addSongToPlaylist("Pop", song);

        assertThat(updated.getCanciones()).hasSize(1);
        assertThat(updated.getCanciones().get(0).getTitulo()).isEqualTo("Levitating");
        assertThat(updated.getCanciones().get(0).getPlaylist().getNombre()).isEqualTo("Pop");

        verify(repository).save(updated);
    }

}
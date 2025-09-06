package com.playlist_backend.services;

import static org.junit.jupiter.api.Assertions.*;

import com.playlist_backend.models.Playlist;
import com.playlist_backend.models.Song;
import com.playlist_backend.repositories.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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
}
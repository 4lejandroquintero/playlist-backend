package com.playlist_backend.services;

import com.playlist_backend.exceptions.ExcepcionNegocio;
import com.playlist_backend.models.Playlist;
import com.playlist_backend.repositories.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    private final PlaylistRepository repository;

    public PlaylistService(PlaylistRepository repository) {
        this.repository = repository;
    }

    public Playlist save(Playlist playlist) {
        if (playlist.getNombre() == null || playlist.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la lista no puede ser nulo o vacío");
        }

        if (repository.existsById(playlist.getNombre())) {
            throw new IllegalStateException("La lista ya existe");
        }
        if (!playlist.getNombre().matches("[a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("El nombre contiene caracteres inválidos");
        }

        return repository.save(playlist);
    }

    public List<Playlist> findAll() {
        return repository.findAll();
    }

    public Optional<Playlist> findByName(String nombre) {
        return repository.findById(nombre);
    }

    public void deleteByName(String nombre) {
        if (!exists(nombre)) {
            throw new ExcepcionNegocio("Lista no encontrada");
        }
        repository.deleteById(nombre);
    }

    public boolean exists(String nombre) {
        return repository.existsById(nombre);
    }

    public List<Playlist> findByNameContaining(String name) {
        return repository.findByNombreContainingIgnoreCase(name);
    }
}

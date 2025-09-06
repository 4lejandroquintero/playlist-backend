package com.playlist_backend.services;

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

    public Playlist save(Playlist p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la lista no puede ser nulo o vacío");
        }

        if (repository.existsById(p.getNombre())) {
            throw new IllegalStateException("La lista ya existe");
        }
        if (!p.getNombre().matches("[a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("El nombre contiene caracteres inválidos");
        }

        return repository.save(p);
    }

    public List<Playlist> findAll() {
        return repository.findAll();
    }

    public Optional<Playlist> findByName(String nombre) {
        return repository.findById(nombre);
    }

    public void deleteByName(String nombre) {
        repository.deleteById(nombre);
    }

    public boolean exists(String nombre) {
        return repository.existsById(nombre);
    }

    public List<Playlist> findByNameContaining(String name) {
        return repository.findByNombreContainingIgnoreCase(name);
    }
}

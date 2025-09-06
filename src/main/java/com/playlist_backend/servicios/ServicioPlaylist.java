package com.playlist_backend.servicios;

import com.playlist_backend.excepciones.ExcepcionNegocio;
import com.playlist_backend.excepciones.ExcepcionTecnica;
import com.playlist_backend.modelos.Playlist;
import com.playlist_backend.modelos.Cancion;
import com.playlist_backend.repositorios.RepositorioPlaylist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioPlaylist {

    private final RepositorioPlaylist repository;

    public ServicioPlaylist(RepositorioPlaylist repository) {
        this.repository = repository;
    }

    public Playlist save(Playlist playlist) {
        validarPlaylist(playlist);
        try {
            return repository.save(playlist);
        } catch (Exception e) {
            throw new ExcepcionTecnica("Error al guardar la lista", e);
        }
    }

    private void validarPlaylist(Playlist playlist) {
        if (playlist.getNombre() == null || playlist.getNombre().isBlank()) {
            throw new ExcepcionNegocio("El nombre de la lista no puede ser nulo o vacío");
        }
        if (!playlist.getNombre().matches("[a-zA-Z0-9 ]+")) {
            throw new ExcepcionNegocio("El nombre contiene caracteres inválidos");
        }
        if (repository.existsById(playlist.getNombre())) {
            throw new ExcepcionNegocio("La lista ya existe");
        }
    }

    public Playlist addSongToPlaylist(String listName, Cancion cancion) {
        Playlist playlist = repository.findById(listName)
                .orElseThrow(() -> new ExcepcionNegocio("Lista no encontrada"));

        boolean exists = playlist.getCanciones().stream()
                .anyMatch(s -> s.getTitulo().equalsIgnoreCase(cancion.getTitulo()));
        if (exists) {
            throw new ExcepcionNegocio("La canción ya está en la lista");
        }

        cancion.setPlaylist(playlist);
        playlist.getCanciones().add(cancion);

        try {
            return repository.save(playlist);
        } catch (Exception e) {
            throw new ExcepcionTecnica("Error al agregar la canción", e);
        }
    }

    public List<Playlist> findAll() {
        return repository.findAll();
    }

    public Playlist getByName(String nombre) {
        return repository.findById(nombre)
                .orElseThrow(() -> new ExcepcionNegocio("Lista no encontrada"));
    }

    public void deleteByName(String nombre) {
        if (!repository.existsById(nombre)) {
            throw new ExcepcionNegocio("Lista no encontrada");
        }
        try {
            repository.deleteById(nombre);
        } catch (Exception e) {
            throw new ExcepcionTecnica("Error al eliminar la lista", e);
        }
    }

    public boolean exists(String nombre) {
        return repository.existsById(nombre);
    }

    public Optional<Playlist> findByName(String nombre) {
        return repository.findById(nombre);
    }

    public List<Playlist> findByNameContaining(String name) {
        return repository.findByNombreContainingIgnoreCase(name);
    }
}

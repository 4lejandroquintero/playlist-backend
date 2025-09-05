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


    public boolean exists(String nombre) { return repository.existsById(nombre); }


}
package com.playlist_backend.controllers;

import com.playlist_backend.models.Playlist;
import com.playlist_backend.models.Song;
import com.playlist_backend.services.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/lists")
public class PlaylistController {

    private final PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Playlist playlist) {
        if (playlist.getNombre() == null || playlist.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre de la lista no puede ser nulo o vacío");
        }
        if (service.exists(playlist.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La lista ya existe");
        }


        Playlist saved = service.save(playlist);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{name}")
                .buildAndExpand(saved.getNombre())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PostMapping("/{listName}/songs")
    public ResponseEntity<Playlist> addSongToPlaylist(
            @PathVariable String listName,
            @RequestBody Song song
    ) {
        return service.findByName(listName)
                .map(playlist -> {
                    song.setPlaylist(playlist);
                    playlist.getCanciones().add(song);
                    Playlist updated = service.save(playlist);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Playlist> getAll() {
        return service.findAll();
    }

    @GetMapping("/{listName}")
    public ResponseEntity<?> getOne(@PathVariable String listName) {
        return service.findByName(listName)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lista no encontrada"));
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<?> delete(@PathVariable String listName) {
        if (!service.exists(listName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lista no encontrada");
        }
        service.deleteByName(listName);
        return ResponseEntity.noContent().build();
    }
}
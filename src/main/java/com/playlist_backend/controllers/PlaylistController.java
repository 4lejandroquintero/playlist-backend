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
        try {
            Playlist saved = service.save(playlist);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{name}")
                    .buildAndExpand(saved.getNombre())
                    .toUri();
            return ResponseEntity.created(location).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/{listName}/songs")
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable String listName,
            @RequestBody Song song
    ) {
        return service.findByName(listName)
                .map(playlist -> {
                    // Validación: no permitir canciones repetidas
                    boolean exists = playlist.getCanciones().stream()
                            .anyMatch(s -> s.getTitulo().equalsIgnoreCase(song.getTitulo()));
                    if (exists) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("La canción ya está en la lista");
                    }

                    song.setPlaylist(playlist);
                    playlist.getCanciones().add(song);
                    Playlist updated = service.save(playlist);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Lista no encontrada"));
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

    @GetMapping("/search")
    public List<Playlist> search(@RequestParam String name) {
        return service.findByNameContaining(name);
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> delete(@PathVariable String listName) {
        service.deleteByName(listName);
        return ResponseEntity.noContent().build();
    }
}
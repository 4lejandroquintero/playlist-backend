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
    public ResponseEntity<Playlist> create(@RequestBody Playlist playlist) {
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
        Playlist updated = service.addSongToPlaylist(listName, song);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<Playlist> getAll() {
        return service.findAll();
    }

    @GetMapping("/{listName}")
    public ResponseEntity<Playlist> getOne(@PathVariable String listName) {
        Playlist playlist = service.getByName(listName);
        return ResponseEntity.ok(playlist);
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

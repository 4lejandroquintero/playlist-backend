package com.playlist_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String artista;
    private String album;
    private String anno;
    private String genero;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonBackReference
    private Playlist playlist;

    public Song() {}

    public Song(String titulo, String artista, String album, String anno, String genero) {
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.anno = anno;
        this.genero = genero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

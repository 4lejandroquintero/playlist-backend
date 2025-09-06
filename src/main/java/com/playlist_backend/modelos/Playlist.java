package com.playlist_backend.modelos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Entity
@NoArgsConstructor
public class Playlist {

    @Id
    private String nombre;

    private String descripcion;

    @Getter
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Cancion> canciones = new ArrayList<>();

    public Playlist(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones.clear();
        if (canciones != null) {
            canciones.forEach(c -> c.setPlaylist(this));
            this.canciones.addAll(canciones);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist playlist)) return false;
        return Objects.equals(nombre, playlist.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}

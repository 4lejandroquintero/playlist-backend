package com.playlist_backend.repositorios;

import com.playlist_backend.modelos.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RepositorioPlaylist extends JpaRepository<Playlist, String> {

    List<Playlist> findByNombreContainingIgnoreCase(String nombre);

}
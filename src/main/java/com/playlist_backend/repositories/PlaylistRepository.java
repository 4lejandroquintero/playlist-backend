package com.playlist_backend.repositories;

import com.playlist_backend.models.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PlaylistRepository extends JpaRepository<Playlist, String> {

}
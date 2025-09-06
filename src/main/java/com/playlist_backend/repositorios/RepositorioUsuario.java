package com.playlist_backend.repositorios;

import com.playlist_backend.modelos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RepositorioUsuario extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
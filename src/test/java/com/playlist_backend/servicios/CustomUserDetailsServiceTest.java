package com.playlist_backend.servicios;

import com.playlist_backend.modelos.User;
import com.playlist_backend.repositorios.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        User user = new User();
        user.setUsername("alejo");
        user.setPassword("12345");
        user.setRole("ROLE_USER");

        when(repositorioUsuario.findByUsername("alejo"))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("alejo");

        // Assert
        assertNotNull(result);
        assertEquals("alejo", result.getUsername());
        assertEquals("12345", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(repositorioUsuario, times(1)).findByUsername("alejo");
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // Arrange
        when(repositorioUsuario.findByUsername("noexiste"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("noexiste"));

        verify(repositorioUsuario, times(1)).findByUsername("noexiste");
    }


}
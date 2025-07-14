package com.perfulandia.usuarioservice.service;

import com.perfulandia.usuarioservice.exception.ResourceNotFoundException;
import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest{
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "Carlos Bittner", "car.bittner@duocuc.cl", "CLIENTE");
    }

    @Test
    @DisplayName("Test 1 - Listar todos los usuarios")
    void listarTodosLosUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario, new Usuario(2L, "Benjamin Martinez", "benjamin@duocuc.cl", "ADMIN"));
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 2 - Guardar nuevo usuario")
    void guardarNuevoUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.guardar(usuario);

        assertNotNull(resultado);
        assertEquals(usuario.getNombre(), resultado.getNombre());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Test 3 - Buscar usuario por id")
    void buscarUsuarioPorId() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscar(usuario.getId());

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        verify(usuarioRepository, times(1)).findById(usuario.getId());
    }

    @Test
    @DisplayName("Test 4 - Buscar usuario por id cuando usuario NO existe")
    void buscarUsuarioCuandoNoExiste() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,  () -> usuarioService.buscar(usuario.getId()));

        assertTrue(exception.getMessage().contains("Usuario no encontrado con ID: "+usuario.getId()));
        verify(usuarioRepository, times(1)).findById(usuario.getId());
    }

    @Test
    @DisplayName("Test 5 - Eliminar usuario por id")
    void eliminarUsuarioPorId() {
        when(usuarioRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminar(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
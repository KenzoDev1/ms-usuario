package com.perfulandia.usuarioservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.service.UsuarioService;
import com.perfulandia.usuarioservice.assemblers.UsuarioModelAssembler;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UsuarioController.class, UsuarioModelAssembler.class})
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "Carlos Bittner", "car.bittner@duocuc.cl", "CLIENTE");
    }

    @Test
    @DisplayName("Test 1 - Debería listar todos los usuarios")
    void listarUsuarios() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        given(usuarioService.listar()).willReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.usuarios[0].nombre", is(usuario.getNombre())));
    }

    @Test
    @DisplayName("Test 2 - Debería guardar un nuevo usuario")
    void guardarUsuario() throws Exception {
        given(usuarioService.guardar(any(Usuario.class))).willReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(usuario.getNombre()))); //
    }

    @Test
    @DisplayName("Test 3 - debería buscar un usuario por ID")
    void buscarUsuarioPorId() throws Exception {
        given(usuarioService.buscar(1L)).willReturn(usuario);

        mockMvc.perform(get("/api/usuarios/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("Test 4 - Debería eliminar un usuario por ID")
    void eliminarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
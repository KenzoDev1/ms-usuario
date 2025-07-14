package com.perfulandia.usuarioservice.service;

import com.perfulandia.usuarioservice.exception.ResourceNotFoundException;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo){
        this.repo=repo;
    }
    //Listar
    public List<Usuario> listar(){
        return repo.findAll();
    }
    //Guardar
    public Usuario guardar(Usuario usuario){
        return repo.save(usuario);
    }
    //Buscar por id
    public Usuario buscar(long id){
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }
    //Eliminar por id
    public void eliminar(long id){
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }
}

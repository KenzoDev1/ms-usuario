package com.perfulandia.usuarioservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder //Crear objetos de manera flexible = Constructor Flex
@Relation(collectionRelation = "usuarios", itemRelation = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String correo;
    private String rol; // ADMIN, GERENTE, Usuario
}

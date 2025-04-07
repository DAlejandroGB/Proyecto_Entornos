
package com.entornos.project.Demo.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;


@Entity
@Table(
        name = "credenciales"
)
@Data
public class Credencial {
    public static final String NAME = "credenciales";
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "credencial_id"
    )
    private Long credencialId;
    @ManyToOne
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;
    @Column(
            name = "usuario",
            nullable = false,
            unique = true
    )
    private String usuarioNombre;
    @Column(
            name = "contrasena",
            nullable = false
    )
    private String contrasena;
    @Column(
            name = "fecha_creacion",
            nullable = false,
            updatable = false
    )
    private Timestamp fechaCreacion;
}
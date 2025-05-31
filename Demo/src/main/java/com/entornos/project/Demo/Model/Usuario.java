package com.entornos.project.Demo.Model;


import com.entornos.project.Demo.DTO.CreateUsuarioDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name=Usuario.TABLE_NAME)
public class Usuario implements Serializable {
    public static final String TABLE_NAME = "usuarios";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name="direccion")
    private String direccion;

    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private Rol rol;

    public Usuario(CreateUsuarioDTO createUsuarioDTO) {
        this.nombres = createUsuarioDTO.getNombres();
        this.apellidos = createUsuarioDTO.getApellidos();
        this.email = createUsuarioDTO.getEmail();
        this.telefono = createUsuarioDTO.getTelefono();
        this.direccion = createUsuarioDTO.getDireccion();
        this.idRol = createUsuarioDTO.getIdRol();
        this.fechaCreacion = LocalDate.now();
    }
}

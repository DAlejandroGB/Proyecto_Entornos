package com.entornos.project.Demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "orden")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_estado")
    private Long idEstado;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "fecha_completado")
    private LocalDate fechaCompletada;

    @Column(name = "fecha_rechazo")
    private LocalDate fechaRechazo;

    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion;

    //Relaciones
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", insertable = false, updatable = false)
    private Estado estado;

    public Orden(Long idUsuario) {
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDate.now();
    }
}

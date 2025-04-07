package com.entornos.project.Demo.Model;

import com.entornos.project.Demo.Enum.Estado;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table
@Entity(name = "ordenes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @Column(name = "usuario_id")
    private Long idUsuario;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    //Relaciones
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;
}

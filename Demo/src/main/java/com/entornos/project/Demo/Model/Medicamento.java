package com.entornos.project.Demo.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Medicamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicamentoId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Lob
    private byte[] imagenMed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    public enum Tipo {
        RECETADO, VENTALIBRE
    }

}
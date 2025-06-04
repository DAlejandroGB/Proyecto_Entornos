package com.entornos.project.Demo.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "orden_medicamentos")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenMedicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_orden")
    private Long idOrden;

    @Column(name = "id_medicamento")
    private Long idMedicamento;

    private Integer cantidad;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicamento", insertable = false, updatable = false)
    private Medicamento medicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden", insertable = false, updatable = false)
    private Orden orden;
}

package com.entornos.project.Demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "ordenesmedicamentos")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenMedicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orden_medicamento_id")
    private Long id;
    @Column(name = "orden_id")
    private Long idOrden;
    @Column(name = "medicamento_id")
    private Long idMedicamento;
    private Integer cantidad;
    @Lob
    @Column(name = "imagen_orden")
    private byte[] imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", insertable = false, updatable = false)
    private Medicamento medicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", insertable = false, updatable = false)
    private Orden orden;
}

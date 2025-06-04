package com.entornos.project.Demo.Model;

import com.entornos.project.Demo.DTO.CrearMedicamentoDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Double precio;
    private String imagen;
    @Column(name = "venta_libre")
    private Boolean ventaLibre;
    private Boolean activo;

    public Medicamento(CrearMedicamentoDTO crearMedicamentoDTO) {
        this.nombre = crearMedicamentoDTO.getNombre();
        this.precio = crearMedicamentoDTO.getPrecio();
        this.imagen = crearMedicamentoDTO.getImagenMed();
        this.ventaLibre = crearMedicamentoDTO.getVentaLibre();
        this.activo = true;
    }
}
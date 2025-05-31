package com.entornos.project.Demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarMedicamentoDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private String imagenMed;
    private Boolean ventaLibre;

}

package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.Medicamento.Tipo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearMedicamentoDTO {

    private String nombre;
    private Double precio;
    private byte[] imagenMed;
    private Tipo tipo;

}
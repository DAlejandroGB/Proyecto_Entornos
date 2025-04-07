package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.Medicamento.Tipo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentoDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private Tipo tipo;

}

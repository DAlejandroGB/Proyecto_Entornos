package com.entornos.project.Demo.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentoDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private Boolean ventaLibre;

}

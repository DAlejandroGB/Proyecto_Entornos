package com.entornos.project.Demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemMedicamentoDTO {

    private Long idMedicamento;
    private String nombreMedicamento;
    private Double precioMedicamento;
    private byte[] imagen;
    private Integer cantidad;
}

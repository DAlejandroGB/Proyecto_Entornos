package com.entornos.project.Demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemMedicamentoDTO {

    private String nombreMedicamento;
    private Double precioMedicamento;
    private byte[] imagen;
}

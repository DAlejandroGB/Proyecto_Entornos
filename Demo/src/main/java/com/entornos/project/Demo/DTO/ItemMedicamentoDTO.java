package com.entornos.project.Demo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemMedicamentoDTO {
    @Schema(name = "idMedicamento", description = "Identificador del medicamento")
    private Long idMedicamento;
    @Schema(name = "nombreMedicamento", description = "Nombre del medicamento")
    private String nombreMedicamento;
    @Schema(name = "precioMedicamento", description = "Precio unitario del medicamento")
    private Double precioMedicamento;
    @Schema(name = "imagen", description = "URL de la imagen del medicamento")
    private String imagen;
    @Schema(name = "cantidad", description = "Unidades del medicamento en la orden")
    private Integer cantidad;
}

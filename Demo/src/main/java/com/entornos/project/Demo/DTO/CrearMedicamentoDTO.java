package com.entornos.project.Demo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearMedicamentoDTO {
    @Schema(name = "nombre", description = "Nombre del medicamento")
    private String nombre;
    @Schema(name = "precio", description = "Precio unitario del medicamento")
    private Double precio;
    @Schema(name = "imagenMed", description = "URL de la imagen del medicamento")
    private String imagenMed;
    @Schema(name = "ventaLibre", description = "Identifica si el medicamento es de venta libre o no (1) para SI (0) para NO")
    private Boolean ventaLibre;

}
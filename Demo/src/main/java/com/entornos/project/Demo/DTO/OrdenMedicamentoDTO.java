package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.OrdenMedicamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenMedicamentoDTO {
    @Schema(name = "idOrden", description = "Identificador del la orden")
    private Long idOrden;
    @Schema(name = "idMedicamento", description = "Identificador del medicamento")
    private Long idMedicamento;
    @Schema(name = "cantidad", description = "Unidades de medicamento a ordenar")
    private Integer cantidad;
    @Schema(name = "imagen", description = "Url de la orden medica si aplica")
    private String imagen;

    public OrdenMedicamentoDTO(OrdenMedicamento ordenMedicamento) {
        this.idOrden = ordenMedicamento.getIdOrden();
        this.idMedicamento = ordenMedicamento.getIdMedicamento();
        this.cantidad = ordenMedicamento.getCantidad();
        this.imagen = ordenMedicamento.getImagen();
    }
}

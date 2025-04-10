package com.entornos.project.Demo.dto;

import com.entornos.project.Demo.Model.OrdenMedicamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenMedicamentoDTO {
    private Long idOrden;
    private String nombreMedicamento;
    private Integer cantidad;

    public OrdenMedicamentoDTO(OrdenMedicamento ordenMedicamento) {
        this.idOrden = ordenMedicamento.getIdOrden();
        this.nombreMedicamento = ordenMedicamento.getMedicamento().getNombre();
        this.cantidad = ordenMedicamento.getCantidad();
    }
}

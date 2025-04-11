package com.entornos.project.Demo.DTO;

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
    private Long idMedicamento;
    private Integer cantidad;

    public OrdenMedicamentoDTO(OrdenMedicamento ordenMedicamento) {
        this.idOrden = ordenMedicamento.getIdOrden();
        this.idMedicamento = ordenMedicamento.getIdMedicamento();
        this.nombreMedicamento = ordenMedicamento.getMedicamento().getNombre();
        this.cantidad = ordenMedicamento.getCantidad();
    }
}

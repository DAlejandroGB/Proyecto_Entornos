package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.Orden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenDTO {
    private Long idOrden;
    private String nombreUsuario;
    private LocalDate fechaCreacion;
    private String estado;
    private List<ItemMedicamentoDTO> medicamentos;

    public OrdenDTO(Orden orden) {
        this.idOrden = orden.getId();
        this.nombreUsuario = orden.getUsuario().getNombres().concat(" ").concat(orden.getUsuario().getApellidos()).toUpperCase();
        this.fechaCreacion = orden.getFechaCreacion();
        this.estado = orden.getEstado().getNombre();
        this.medicamentos = new ArrayList<>();
    }
}

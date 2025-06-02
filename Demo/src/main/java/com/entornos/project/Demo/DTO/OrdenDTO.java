package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.Orden;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "idOrden", description = "Identificador del la orden")
    private Long idOrden;
    @Schema(name = "nombreUsuario", description = "Nombre del usuario el cual realizo la orden")
    private String nombreUsuario;
    @Schema(name = "fechaCreacion", description = "Fecha de creacion de la orden")
    private LocalDate fechaCreacion;
    @Schema(name = "estado", description = "Estado en el cual se encuentra la orden", example = "PENDIENTE")
    private String estado;
    @Schema(name = "medicamentos", description = "Listado de medicamentos asociados a la orden")
    private List<ItemMedicamentoDTO> medicamentos;

    public OrdenDTO(Orden orden) {
        this.idOrden = orden.getId();
        this.nombreUsuario = orden.getUsuario() != null ? orden.getUsuario().getNombres().concat(" ").concat(orden.getUsuario().getApellidos()).toUpperCase() : null;
        this.fechaCreacion = orden.getFechaCreacion();
        this.estado = orden.getEstado() != null ? orden.getEstado().getNombre() : null;
        this.medicamentos = new ArrayList<>();
    }
}

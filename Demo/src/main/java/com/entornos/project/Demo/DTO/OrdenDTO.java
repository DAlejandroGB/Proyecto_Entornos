package com.entornos.project.Demo.dto;

import com.entornos.project.Demo.Enum.Estado;
import com.entornos.project.Demo.Model.Orden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenDTO {
    private String nombreUsuario;
    private LocalDateTime fechaCreacion;
    private Estado estado;
    private List<ItemMedicamentoDTO> medicamentos;

    public OrdenDTO(Orden orden) {
        this.nombreUsuario = orden.getUsuario().getNombre().concat(" ").concat(orden.getUsuario().getApellido()).toUpperCase();
        this.fechaCreacion = orden.getFechaCreacion();
        this.estado = orden.getEstado();
        this.medicamentos = new ArrayList<>();
    }
}

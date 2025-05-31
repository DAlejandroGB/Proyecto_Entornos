package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.Model.*;
import com.entornos.project.Demo.Repository.*;
import com.entornos.project.Demo.Service.interfaces.IOrdenService;
import com.entornos.project.Demo.DTO.OrdenDTO;
import com.entornos.project.Demo.DTO.OrdenMedicamentoDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenService implements IOrdenService {

    private OrdenRepository ordenRepository;
    private UsuarioRepository usuarioRepository;
    private MedicamentoRepository medicamentoRepository;
    private OrdenMedicamentoRepository ordenMedicamentoRepository;
    private IEstadoRepository estadoRepository;

    @Transactional
    protected OrdenDTO createOrden(Long idUsuario) {

        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isEmpty()) throw new RuntimeException("No existe el usuario con el id " + idUsuario);

        var ordenPendiente = this.getOrdenPendiente(idUsuario);

        if(ordenPendiente == null) {
            Orden orden = new Orden(idUsuario);
            //Traemos el estado pendiente por defecto
            Estado estadoPendiente = this.estadoRepository.findByNombre("PENDIENNTE");
            orden.setEstado(estadoPendiente);
            return new OrdenDTO(ordenRepository.save(orden));

        }
        return ordenPendiente;
    }

    @Override
    @Transactional
    public OrdenMedicamentoDTO addMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, Long idUsuario) throws IOException {

        OrdenDTO ordenDTO = this.createOrden(idUsuario);
        Medicamento medicamento = this.verificarMedicamento(ordenMedicamentoDTO.getIdMedicamento());

        OrdenMedicamento ordenMedicamentoDB = this.ordenMedicamentoRepository.findMedicamentoByIdOrden(ordenDTO.getIdOrden(), ordenMedicamentoDTO.getIdMedicamento());

        if(ordenMedicamentoDB != null){
            ordenMedicamentoDB.setCantidad(ordenMedicamentoDB.getCantidad() + ordenMedicamentoDTO.getCantidad());
            return new OrdenMedicamentoDTO(ordenMedicamentoRepository.save(ordenMedicamentoDB));
        }

        OrdenMedicamento ordenMedicamento = getOrdenMedicamento(ordenMedicamentoDTO, ordenDTO, medicamento);
        ordenMedicamentoRepository.findById(ordenMedicamentoRepository.save(ordenMedicamento).getId()).orElseThrow();

        return new OrdenMedicamentoDTO();
    }

    private static OrdenMedicamento getOrdenMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, OrdenDTO ordenDTO, Medicamento medicamento) {
        OrdenMedicamento ordenMedicamento = new OrdenMedicamento();
        ordenMedicamento.setIdOrden(ordenDTO.getIdOrden());
        ordenMedicamento.setIdMedicamento(ordenMedicamentoDTO.getIdMedicamento());
        ordenMedicamento.setCantidad(ordenMedicamentoDTO.getCantidad());
        if(!medicamento.getVentaLibre()){
            if(ordenMedicamentoDTO.getImagen() == null) throw new RuntimeException("Su medicamento no es de venta libre, por favor cargue la orden médica.");
            ordenMedicamento.setImagen(ordenMedicamentoDTO.getImagen());
        }
        return ordenMedicamento;
    }

    private Orden verificarOrden(Long idOrden) {
        Optional<Orden> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) throw new RuntimeException("No se encontró el orden");
        return orden.get();
    }

    private Medicamento verificarMedicamento(Long idMedicamento) {
        Optional<Medicamento> medicamento = this.medicamentoRepository.findById(idMedicamento);
        if (medicamento.isEmpty()) throw new RuntimeException("No hay medicamento con el id " + idMedicamento);
        return medicamento.get();
    }

    @Override
    @Transactional
    public void deleteMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO) {
        verificarOrden(ordenMedicamentoDTO.getIdOrden());
        verificarMedicamento(ordenMedicamentoDTO.getIdMedicamento());

        OrdenMedicamento ordenMedicamentoDB = this.ordenMedicamentoRepository.findMedicamentoByIdOrden(ordenMedicamentoDTO.getIdOrden(), ordenMedicamentoDTO.getIdMedicamento());

        if(ordenMedicamentoDB != null){
            if(ordenMedicamentoDB.getCantidad() - ordenMedicamentoDTO.getCantidad() <= 0){
                this.ordenMedicamentoRepository.delete(ordenMedicamentoDB);
            }else{
                ordenMedicamentoDB.setCantidad(ordenMedicamentoDB.getCantidad() - ordenMedicamentoDTO.getCantidad());
                ordenMedicamentoRepository.save(ordenMedicamentoDB);
            }
        }
    }

    @Override
    public OrdenDTO getOrdenPendiente(Long idUsuario) {
        //Traemos el estado pendiente
        Estado estadoPendiente = this.estadoRepository.findByNombre("PENDENTE");
        List<Orden> ordenes = ordenRepository.findByIdUsuarioAndIdEstado(idUsuario, estadoPendiente.getId());
        Orden orden = ordenes.getFirst();
        OrdenDTO ordenDTO = new OrdenDTO(orden);
        //Se consultan los medicamentos asociados a esa orden
        ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(orden.getId()));
        return ordenDTO;
    }

    @Override
    public Page<OrdenDTO> getAllOrdenesByUsuario(Long idUsuario, Pageable pageable) {
        Page<Orden> ordenes = this.ordenRepository.findAllByIdUsuario(idUsuario, pageable);
        List<OrdenDTO> ordenDTOS = ordenes.getContent().stream().map(orden -> {
            OrdenDTO ordenDTO = new OrdenDTO(orden);
            //Se consultan los medicamentos asociados a esa orden
            ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(orden.getId()));
            return ordenDTO;
        }).toList();

        return new PageImpl<>(ordenDTOS, pageable, ordenes.getTotalElements());
    }

    @Override
    public OrdenDTO getOrden(Long idOrden) {
        Optional<Orden> orden = this.ordenRepository.findById(idOrden);

        if(orden.isEmpty()) return null;

        OrdenDTO ordenDTO = new OrdenDTO(orden.get());
        ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(idOrden));

        return ordenDTO;
    }

    @Autowired
    public void setOrdenRepository(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Autowired
    public void setMedicamentoRepository(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Autowired
    public void setOrdenMedicamentoRepository(OrdenMedicamentoRepository ordenMedicamentoRepository) {
        this.ordenMedicamentoRepository = ordenMedicamentoRepository;
    }

    @Autowired
    public void setEstadoRepository(IEstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }
}

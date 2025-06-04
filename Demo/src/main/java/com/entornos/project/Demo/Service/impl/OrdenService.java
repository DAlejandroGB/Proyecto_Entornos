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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
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
            Estado estadoPendiente = this.estadoRepository.findByNombre("PENDIENTE");
            orden.setIdEstado(estadoPendiente.getId());
            orden.setEstado(estadoPendiente);
            return new OrdenDTO(ordenRepository.save(orden));

        }
        return ordenPendiente;
    }

    @Override
    @Transactional
    public OrdenMedicamentoDTO addMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, Long idUsuario, MultipartFile ordenMedica) throws IOException {

        OrdenDTO ordenDTO = this.createOrden(idUsuario);
        Medicamento medicamento = this.verificarMedicamento(ordenMedicamentoDTO.getIdMedicamento());

        OrdenMedicamento ordenMedicamentoDB = this.ordenMedicamentoRepository.findMedicamentoByIdOrden(ordenDTO.getIdOrden(), ordenMedicamentoDTO.getIdMedicamento());

        if(ordenMedicamentoDB != null){
            ordenMedicamentoDB.setCantidad(ordenMedicamentoDB.getCantidad() + ordenMedicamentoDTO.getCantidad());
            return new OrdenMedicamentoDTO(ordenMedicamentoRepository.save(ordenMedicamentoDB));
        }

        OrdenMedicamento ordenMedicamento = getOrdenMedicamento(ordenMedicamentoDTO, ordenDTO, medicamento, ordenMedica);
        ordenMedicamentoRepository.findById(ordenMedicamentoRepository.save(ordenMedicamento).getId()).orElseThrow();

        return new OrdenMedicamentoDTO(ordenMedicamento);
    }

    @Transactional
    public OrdenMedicamento getOrdenMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, OrdenDTO ordenDTO, Medicamento medicamento, MultipartFile ordenMedica) throws IOException {
        OrdenMedicamento ordenMedicamento = new OrdenMedicamento();
        ordenMedicamento.setIdOrden(ordenDTO.getIdOrden());
        ordenMedicamento.setIdMedicamento(ordenMedicamentoDTO.getIdMedicamento());
        ordenMedicamento.setCantidad(ordenMedicamentoDTO.getCantidad());
        if(!medicamento.getVentaLibre()){
            if(ordenMedica == null) throw new RuntimeException("Su medicamento no es de venta libre, por favor cargue la orden médica.");
            ordenMedicamento.setImagen(Base64.getEncoder().encodeToString(ordenMedica.getBytes()));
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
        Estado estadoPendiente = this.estadoRepository.findByNombre("PENDIENTE");
        List<Orden> ordenes = ordenRepository.findByIdUsuarioAndIdEstado(idUsuario, estadoPendiente.getId());
        if(ordenes.isEmpty()) return null;
        Orden orden = ordenes.getFirst();
        OrdenDTO ordenDTO = new OrdenDTO(orden);
        //Se consultan los medicamentos asociados a esa orden
        ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(orden.getId()).stream().peek(med -> med.setPrecioMedicamento(med.getPrecioMedicamento()*med.getCantidad())).toList());
        return ordenDTO;
    }

    @Override
    public Page<OrdenDTO> getAllOrdenesByUsuario(Long idUsuario, Pageable pageable) {
        Page<Orden> ordenes = this.ordenRepository.findAllByIdUsuario(idUsuario, pageable);
        List<OrdenDTO> ordenDTOS = ordenes.getContent().stream().map(orden -> {
            OrdenDTO ordenDTO = new OrdenDTO(orden);
            //Se consultan los medicamentos asociados a esa orden
            ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(orden.getId()).stream().peek(med -> med.setPrecioMedicamento(med.getPrecioMedicamento()*med.getCantidad())).toList());
            return ordenDTO;
        }).toList();

        return new PageImpl<>(ordenDTOS, pageable, ordenes.getTotalElements());
    }

    @Override
    public OrdenDTO getOrden(Long idOrden) {
        Optional<Orden> orden = this.ordenRepository.findById(idOrden);

        if(orden.isEmpty()) throw new RuntimeException("No se encontro orden con el id " + idOrden);

        OrdenDTO ordenDTO = new OrdenDTO(orden.get());
        ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(idOrden).stream().peek(med -> med.setPrecioMedicamento(med.getPrecioMedicamento()*med.getCantidad())).toList());

        return ordenDTO;
    }

    @Override
    public Page<Orden> getAllOrdenesByEstado(String estado, Pageable pageable) {
        return this.ordenRepository.findAllByEstado(estado, pageable);
    }

    @Override
    @Transactional
    public Orden updateEstadoOrden(Long idOrden, String estado) {
        Orden ordenUpdated = this.ordenRepository.findById(idOrden).orElse(null);
        if (ordenUpdated == null) throw new RuntimeException("No se encontro la orden");
        //Traemos el estado a actualizar
        Estado estadoDB = this.estadoRepository.findByNombre(estado);
        if (estadoDB == null) throw new RuntimeException("No se encontro el estado");
        if(estado.contains("COMPLETADA")){
            if(ordenUpdated.getReciboPago() == null || ordenUpdated.getReciboPago().isEmpty()) throw new RuntimeException("No es posible cambiar el estado, por favor cargue el recibo de pago.");
            ordenUpdated.setFechaCompletada(LocalDate.now());
        }
        if(estado.contains("CANCELADA")){
            ordenUpdated.setFechaRechazo(LocalDate.now());
        }
        ordenUpdated.setIdEstado(estadoDB.getId());
        ordenUpdated.setFechaModificacion(LocalDate.now());
        return this.ordenRepository.save(ordenUpdated);
    }

    @Override
    public OrdenDTO cargarReciboPago(Long idOrden, MultipartFile reciboPago) throws IOException {
        Orden orden = this.ordenRepository.findById(idOrden).orElse(null);
        if (orden == null) throw new RuntimeException("No se encontro la orden");

        orden.setReciboPago(Base64.getEncoder().encodeToString(reciboPago.getBytes()));
        orden.setFechaModificacion(LocalDate.now());
        this.ordenRepository.save(orden);

        OrdenDTO ordenDTO = new OrdenDTO(orden);
        ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(idOrden).stream().peek(med -> med.setPrecioMedicamento(med.getPrecioMedicamento()*med.getCantidad())).toList());
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

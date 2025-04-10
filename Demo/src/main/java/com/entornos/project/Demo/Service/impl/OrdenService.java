package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.Model.Medicamento;
import com.entornos.project.Demo.Model.Orden;
import com.entornos.project.Demo.Model.OrdenMedicamento;
import com.entornos.project.Demo.Model.Usuario;
import com.entornos.project.Demo.Repository.MedicamentoRepository;
import com.entornos.project.Demo.Repository.OrdenMedicamentoRepository;
import com.entornos.project.Demo.Repository.OrdenRepository;
import com.entornos.project.Demo.Repository.UsuarioRepository;
import com.entornos.project.Demo.Service.interfaces.IOrdenService;
import com.entornos.project.Demo.dto.OrdenDTO;
import com.entornos.project.Demo.dto.OrdenMedicamentoDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenService implements IOrdenService {

    private OrdenRepository ordenRepository;
    private UsuarioRepository usuarioRepository;
    private MedicamentoRepository medicamentoRepository;
    private OrdenMedicamentoRepository ordenMedicamentoRepository;

    @Override
    @Transactional
    public OrdenDTO createOrden(Long idUsuario) {

        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isEmpty()) throw new RuntimeException("No existe el usuario con el id " + idUsuario);

        Orden orden = new Orden(idUsuario);
        Orden ordenDB = ordenRepository.save(orden);
        return new OrdenDTO(ordenDB);
    }

    @Override
    @Transactional
    public OrdenMedicamentoDTO addMedicamento(Long idOrden, Long idMedicamento, Integer cantidad, MultipartFile imagen) throws IOException {
        Optional<Orden> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) throw new RuntimeException("No encontró el orden");

        Orden ordenDB = orden.get();

        Optional<Medicamento> medicamento = this.medicamentoRepository.findById(idMedicamento);
        if (medicamento.isEmpty()) throw new RuntimeException("No hay medicamento con el id " + idMedicamento);

        OrdenMedicamento ordenMedicamento = new OrdenMedicamento();
        ordenMedicamento.setIdOrden(idOrden);
        ordenMedicamento.setIdMedicamento(idMedicamento);
        ordenMedicamento.setCantidad(cantidad);
        ordenMedicamento.setImagen(imagen.getBytes());

        return new OrdenMedicamentoDTO(ordenMedicamentoRepository.save(ordenMedicamento));
    }

    @Override
    public List<OrdenDTO> getAllOrdenes() {
        List<Orden> ordenes = this.ordenRepository.findAll();
        return ordenes.stream().map(orden -> {
            OrdenDTO ordenDTO = new OrdenDTO(orden);
            //Se consultan los medicamentos asociados a esa orden
            ordenDTO.setMedicamentos(this.ordenMedicamentoRepository.findAllByIdOrden(orden.getId()));
            return ordenDTO;
        }).toList();
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
}

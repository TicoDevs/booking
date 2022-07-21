package org.mfc.booking.servicio;

import org.mfc.booking.dto.CitaContent;
import org.mfc.booking.dto.CitaDto;
import org.mfc.booking.entidad.Cita;
import org.mfc.booking.excepcion.ResourceNotFoundException;
import org.mfc.booking.repositorio.CitaRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CitaServicioImpl implements CitaServicio{
    @Autowired
    CitaRepositorio citaRepositorio;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    public List<CitaDto> listar() {
        List<Cita> citaList = citaRepositorio.findAll();
        List<CitaDto> citaDtoList = citaList.stream().map(cita -> mappearDTO(cita)).collect(Collectors.toList());
        return citaDtoList;
    }

    @Override
    public CitaContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir) {
        Sort sort  = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(ordernarPor).ascending():Sort.by(ordernarPor).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Cita> citas = citaRepositorio.findAll(pageable);
        List<Cita> citaList = citas.getContent();
        List<CitaDto> contenido = citaList.stream().map(cita -> mappearDTO(cita))
                .collect(Collectors.toList());
        CitaContent citaContent = new CitaContent();
        citaContent.setNumeroPaginas(citas.getNumber());
        citaContent.setMedidaPagina(citas.getSize());
        citaContent.setTotalElementos(citas.getTotalElements());
        citaContent.setTotalPaginas(citas.getTotalPages());
        citaContent.setUltima(citas.isLast());
        citaContent.setContenido(contenido);
        return citaContent;
    }

    @Override
    public CitaDto obtenerCitaPorId(Long Id) {
        Cita cita =  citaRepositorio.findById(Id)
                .orElseThrow(() ->new ResourceNotFoundException("Cita","Id", String.valueOf(Id)));
        return mappearDTO(cita);
    }

    @Override
    public boolean existePorId(long id) {
        return citaRepositorio.existsById(id);
    }

    @Override
    public CitaDto crear(CitaDto citaDto) {
        //Convertimos de DTO a entidad
        Cita cita = mappearEntidad(citaDto);
        Cita nueva = citaRepositorio.save(cita);
        //Convertimos de entidad a DTO
        CitaDto citaDtoRepuesta = mappearDTO(nueva);
        return citaDtoRepuesta;
    }

    @Override
    public CitaDto actualizarCita(CitaDto citaDto, long id) {
        Cita cita = citaRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "Id", String.valueOf(id)));
        cita.setFechaCita(citaDto.getFechaCita());
        cita.setEstado(citaDto.getEstado());
        cita.setDescripcion(citaDto.getDescripcion());
        Cita citaActualizada = citaRepositorio.save(cita);
        return mappearDTO(citaActualizada);

    }

    @Override
    public void eliminarCita(long id) {
        Cita cita = citaRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "Id", String.valueOf(id)));
        citaRepositorio.delete(cita);
    }

    //Convierte entidad a DTO
    private CitaDto mappearDTO (Cita cita){
        CitaDto citaDto = modelMapper.map(cita, CitaDto.class);
        return citaDto;
    }

    //Convierte DTO a entidad
    private Cita mappearEntidad (CitaDto citaDto){
        Cita cita = modelMapper.map(citaDto, Cita.class);
        return cita;
    }


}

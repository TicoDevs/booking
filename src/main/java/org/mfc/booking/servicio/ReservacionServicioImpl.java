package org.mfc.booking.servicio;

import org.mfc.booking.dto.ReservacionContent;
import org.mfc.booking.dto.ReservacionDto;
import org.mfc.booking.entidad.Reservacion;
import org.mfc.booking.excepcion.ResourceNotFoundException;
import org.mfc.booking.repositorio.ReservacionRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservacionServicioImpl implements  ReservacionServicio{

    @Autowired
    ReservacionRepositorio reservacionRepositorio;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<ReservacionDto> listar() {
        List<Reservacion> reservacionList = reservacionRepositorio.findAll();
        List<ReservacionDto> reservacionDtoList = reservacionList.stream().map(usuario -> mappearDTO(usuario)).collect(Collectors.toList());
        return reservacionDtoList;
    }

    @Override
    public ReservacionContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir) {
        Sort sort  = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(ordernarPor).ascending():Sort.by(ordernarPor).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Reservacion> reservaciones = reservacionRepositorio.findAll(pageable);
        List<Reservacion> reservacionList = reservaciones.getContent();
        List<ReservacionDto> contenido = reservacionList.stream().map(reservacion -> mappearDTO(reservacion))
                .collect(Collectors.toList());
        ReservacionContent reservacionContent = new ReservacionContent();
        reservacionContent.setNumeroPaginas(reservaciones.getNumber());
        reservacionContent.setMedidaPagina(reservaciones.getSize());
        reservacionContent.setTotalElementos(reservaciones.getTotalElements());
        reservacionContent.setTotalPaginas(reservaciones.getTotalPages());
        reservacionContent.setUltima(reservaciones.isLast());
        reservacionContent.setContenido(contenido);
        return reservacionContent;
    }



    @Override
    public ReservacionDto obtenerReservacionPorId(Long Id) {
        Reservacion reservacion =  reservacionRepositorio.findById(Id)
                .orElseThrow(() ->new ResourceNotFoundException("Reservacion","Id", String.valueOf(Id)));
        return mappearDTO(reservacion);
    }

    @Override
    public boolean existePorId(long id) {
        return reservacionRepositorio.existsById(id);
    }


    @Override
    public ReservacionDto crear(ReservacionDto reservacionDto) {
        //Convertimos de DTO a entidad
        Reservacion reservacion = mappearEntidad(reservacionDto);
        Reservacion nueva = reservacionRepositorio.save(reservacion);
        //Convertimos de entidad a DTO
        ReservacionDto reservacionDtoRespuesta = mappearDTO(nueva);
        return reservacionDtoRespuesta;
    }

    @Override
    public ReservacionDto actualizarReservacion(ReservacionDto reservacionDto, long id) {

        Reservacion reservacion = reservacionRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservacion", "Id", String.valueOf(id)));
        reservacion.setFechaReservacion(reservacionDto.getFechaReservacion());
        reservacion.setEstado(reservacionDto.getEstado());
        reservacion.setTipo(reservacion.getTipo());
        reservacion.setIdMiembro(reservacionDto.getIdMiembro());
        reservacion.setProdSet(reservacionDto.getProdSet());
        reservacion.setIdCita(reservacionDto.getIdCita());

        Reservacion reservacionActualizada = reservacionRepositorio.save(reservacion);
        return mappearDTO(reservacionActualizada);
    }

    @Override
    public void eliminarReservacion(long id) {
        Reservacion reservacion = reservacionRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservacion", "Id", String.valueOf(id)));
        reservacionRepositorio.delete(reservacion);

    }
    private ReservacionDto mappearDTO (Reservacion reservacion){
        ReservacionDto reservacionDto = modelMapper.map(reservacion, ReservacionDto.class);
        return reservacionDto;
    }
    private Reservacion mappearEntidad(ReservacionDto reservacionDto) {
        Reservacion reservacion = modelMapper.map(reservacionDto, Reservacion.class);
        return reservacion;
    }
}

package org.mfc.booking.servicio;

import org.mfc.booking.dto.*;
import org.mfc.booking.entidad.DetalleReservacionProd;
import org.mfc.booking.entidad.Producto;
import org.mfc.booking.entidad.Reservacion;
import org.mfc.booking.excepcion.ResourceNotFoundException;
import org.mfc.booking.repositorio.ReservacionRepositorio;
import org.mfc.booking.seguridad.entidad.Usuario;
import org.mfc.booking.seguridad.servicio.UsuarioServicio;
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
    ProductoServicio productoServicio;
    @Autowired
    UsuarioServicio usuarioServicio;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<ReservacionDto> listar() {
        List<Reservacion> reservacionList = reservacionRepositorio.findAll();

        List<ReservacionDto> reservacionDtoList = reservacionList.stream().map(reservacion -> mappearDTO(reservacion)).collect(Collectors.toList());
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
    @Transactional(readOnly = true)
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
    public ReservacionDto crearReservProd(ReservacionDto reservacionDto) {
        //Convertimos de DTO a entidad
        Reservacion nuevaReserv = new Reservacion();
        nuevaReserv.setFechaReservacion(reservacionDto.getFechaReservacion());
        nuevaReserv.setEstado(reservacionDto.getEstado());
        nuevaReserv.setTipo(reservacionDto.getTipo());
        UsuarioDto usuarioDto = usuarioServicio.obtenerUsuarioPorId(reservacionDto.getUsuario().getId());
        nuevaReserv.setUsuario(mappearEntidadUsu(usuarioDto));
        Set<DetalleReservacionProd> detProd = new HashSet<>();
        for (DetalleReservacionProdDto det: reservacionDto.getProd() ) {
            DetalleReservacionProd detNuevo = new DetalleReservacionProd();
            detNuevo.setCantidad(det.getCantidad());
            detNuevo.setProducto(mappearEntidadProd(det.getProducto()));
            detProd.add(detNuevo);
        }
        nuevaReserv.setProd(detProd);
        nuevaReserv.setDetalleReserva(reservacionDto.getDetalleReserva());
        Reservacion nueva = reservacionRepositorio.save(nuevaReserv);
        //Convertimos de entidad a DTO
         ReservacionDto reservacionDtoRespuesta = mappearDTO(nueva);
        return reservacionDtoRespuesta;
    }
    @Override
    public ReservacionDto crearReservCita(ReservacionDto reservacionDto) {
        //Convertimos de DTO a entidad
        Reservacion nuevaReserv = new Reservacion();
        nuevaReserv.setFechaReservacion(reservacionDto.getFechaReservacion());
        nuevaReserv.setEstado(reservacionDto.getEstado());
        nuevaReserv.setTipo(reservacionDto.getTipo());
        UsuarioDto usuarioDto = usuarioServicio.obtenerUsuarioPorId(reservacionDto.getUsuario().getId());
        nuevaReserv.setUsuario(mappearEntidadUsu(usuarioDto));
        nuevaReserv.setDetalleReserva(reservacionDto.getDetalleReserva());
        Reservacion nueva = reservacionRepositorio.save(nuevaReserv);
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
        Reservacion reservacionActualizada = reservacionRepositorio.save(reservacion);
        return mappearDTO(reservacionActualizada);
    }

    @Override
    @Transactional(readOnly = true)
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

    private Producto mappearEntidadProd(ProductoDto productoDto) {
        Producto producto = modelMapper.map(productoDto, Producto.class);
        return producto;
    }

    private Usuario mappearEntidadUsu(UsuarioDto usuarioDto) {
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);
        return usuario;
    }
}

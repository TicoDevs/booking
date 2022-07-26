package org.mfc.booking.controlador;

import org.mfc.booking.dto.*;
import org.mfc.booking.servicio.*;
import org.mfc.booking.util.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.mfc.booking.constantes.AppConstantes.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/reservaciones")
public class ReservacionControlador {

    @Autowired
    ReservacionServicio reservacionService;
    @Autowired
    DetalleReservacionProdServicio detalleReservacionProdServicio;
    @Autowired
    ProductoServicio productoService;
    @Autowired
    CitaServicio citaService;
    @Autowired
    BitacoraServicio bitacoraServicio;
    @Autowired
    private Utilidades util;



    @PostMapping("/crearReservacion")
    public ResponseEntity<ReservacionDto> crearReservacion(@RequestBody ReservacionDto reservacionDto){
        Set<String> salida = new HashSet<>();
        ReservacionDto nuevaReserv = new ReservacionDto();
        nuevaReserv.setFechaReservacion(reservacionDto.getFechaReservacion());
        nuevaReserv.setEstado(reservacionDto.getEstado());
        nuevaReserv.setTipo(reservacionDto.getTipo());
        nuevaReserv.setUsuario(reservacionDto.getUsuario());
        nuevaReserv.setDetalleReserva(reservacionDto.getDetalleReserva());
        Set<DetalleReservacionProdDto> setDet = new HashSet<>();
        Set<ProductoDto> setProd = new HashSet<>();
        for ( DetalleReservacionProdDto dto : reservacionDto.getProd()) {
            ProductoDto prodDto = productoService.obtenerProductoPorId(dto.getProducto().getId());
            if (prodDto.getCantidad() < dto.getCantidad() ){
                salida.add("Libro: " + prodDto.getNombre() + " " + ".No hay cantidad suficiente");
            }else{
                DetalleReservacionProdDto nuevoDet =  new DetalleReservacionProdDto();
                nuevoDet.setCantidad(dto.getCantidad());
                nuevoDet.setProducto(dto.getProducto());
                setDet.add(nuevoDet);
                prodDto.setCantidad(prodDto.getCantidad()-dto.getCantidad());
                setProd.add(prodDto);
            }
        }
         nuevaReserv.setProd(setDet);
        if (salida.isEmpty()){
            reservacionService.crearReservProd(nuevaReserv);
            for (ProductoDto actProd: setProd) {
                productoService.actualizarProductoReserv(actProd, actProd.getId());
            }
            return  new ResponseEntity(new Mensaje("Reserva realizada con exito"),HttpStatus.OK);
        }else{
            return  new ResponseEntity(new Mensaje("No se pudo realizar la reserva." + salida),HttpStatus.OK);
        }
    }

    @PostMapping("/crearReservacionCita")
    public ResponseEntity<?> crearReservacionCita(@RequestBody ReservacionDto reservacionDto){
        ReservacionDto nuevaReserv = new ReservacionDto();
        nuevaReserv.setFechaReservacion(reservacionDto.getFechaReservacion());
        nuevaReserv.setEstado(reservacionDto.getEstado());
        nuevaReserv.setTipo(reservacionDto.getTipo());
        nuevaReserv.setUsuario(reservacionDto.getUsuario());
        nuevaReserv.setCita(reservacionDto.getCita());
        nuevaReserv.setDetalleReserva(reservacionDto.getDetalleReserva());
        if (reservacionService.crearReservCita(nuevaReserv) == null)
          return  new ResponseEntity(new Mensaje("No se pudo realizar la reserva." ),HttpStatus.BAD_REQUEST);
        return  new ResponseEntity(new Mensaje("Reserva realizada con exito." ),HttpStatus.OK);
    }

    @GetMapping("/listarPageSort")
    public ResponseEntity<ProductoContent> listarProductosContent(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_PAGINA_DEFECTO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_PAGINA_DEFECTO, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordernarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCCION_DEFECTO,required = false) String sortDir){
        return new ResponseEntity(reservacionService.listarPagSort(pageNo, pageSize, ordernarPor, sortDir), HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ReservacionDto>> listar(){
        List<ReservacionDto> list = reservacionService.listar();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservacionDto> obtenerReservacionPorId(@PathVariable(name = "id")long id){
        return ResponseEntity.ok(reservacionService.obtenerReservacionPorId(id));
    }

    @PutMapping("/cambiaEstadoReservacion/{id}/{estado}")
    public ResponseEntity<?> cambiaEstadoReservacion(@PathVariable(name = "id")long id, @PathVariable(name = "estado")int estado){
        if(!this.reservacionService.existePorId(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        ReservacionDto reservacionDtoRespuesta =reservacionService.obtenerReservacionPorId(id);
        if (reservacionDtoRespuesta.getTipo() == 0){
            switch (estado){
                case 1:
                    //Equivale a las aceptada
                    UsuarioDto usu = this.util.obtenerUsuarioActual();
                    reservacionDtoRespuesta.setEstado(estado);
                    ReservacionDto actualizada = reservacionService.actualizarReservacion(reservacionDtoRespuesta,reservacionDtoRespuesta.getId());
                    for (DetalleReservacionProdDto prodAct: actualizada.getProd()) {
                        List<BitacoraDto> lstBit = bitacoraServicio.listar().stream().filter(x -> x.getIdProducto() == prodAct.getProducto().getId())
                                .sorted(Comparator.comparing(BitacoraDto::getUltimaModificacion))
                                .collect(Collectors.toList());
                        BitacoraDto bit = lstBit.get(0);
                        bit.setId(Long.valueOf(0));
                        bit.setFechaSalida(new Date());
                        bit.setIdUsuario(usu.getId());
                        bit.setIdUsuarioMiembro(actualizada.getUsuario().getId());
                        bit.setUltimaModificacion(new Date());
                        bit.setDescripcion("Libro: " + prodAct.getProducto().getNombre() + " ISBN:" + prodAct.getProducto().getCodigo() + " Proceso: Entrega Libro" + " /Miembro: " + usu.getNombre());
                        bitacoraServicio.crearBitacora(bit);
                    }
                    break;
                case 2:
                    //Equivale a las rechazada
                    reservacionDtoRespuesta.setEstado(estado);
                    ReservacionDto actualizada1 = reservacionService.actualizarReservacion(reservacionDtoRespuesta,reservacionDtoRespuesta.getId());
                    for (DetalleReservacionProdDto prodAct: actualizada1.getProd()) {
                        ProductoDto productoDto = productoService.obtenerProductoPorId(prodAct.getProducto().getId());
                        productoDto.setCantidad(productoDto.getCantidad() + prodAct.getCantidad());
                        productoService.actProdCambioEstado(productoDto,productoDto.getId());
                    }
                    break;
            }
        }else{
            reservacionDtoRespuesta.setEstado(estado);
            reservacionService.actualizarReservacion(reservacionDtoRespuesta,reservacionDtoRespuesta.getId());
        }

        return  new ResponseEntity(new Mensaje("Cambio de estado con exito."),HttpStatus.OK);
    }


}

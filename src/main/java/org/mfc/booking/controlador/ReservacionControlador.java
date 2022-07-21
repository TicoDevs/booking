package org.mfc.booking.controlador;

import org.mfc.booking.dto.*;
import org.mfc.booking.servicio.CitaServicio;
import org.mfc.booking.servicio.ProductoServicio;
import org.mfc.booking.servicio.ReservacionServicio;
import org.modelmapper.internal.util.Stack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.mfc.booking.constantes.AppConstantes.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/reservaciones")
public class ReservacionControlador {

    @Autowired
    ReservacionServicio reservacionService;
    @Autowired
    ProductoServicio productoService;
    @Autowired
    CitaServicio citaService;


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

   /* @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody ReservacionDto reservacionDto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campo incorrecto"), HttpStatus.BAD_REQUEST);
        ReservacionDto reservacion = new ReservacionDto(reservacionDto.getFechaReservacion(), reservacionDto.getEstado(), reservacionDto.getTipo(), reservacionDto.getIdMiembro(), reservacionDto.getIdProducto(), reservacionDto.getIdCita());
        reservacionService.crear(reservacion);
        return new ResponseEntity(new Mensaje("Reservación guardada"), HttpStatus.CREATED);
    }*/


    @PutMapping("/cambiaEstadoReservacion/{id}")
    public ResponseEntity<ReservacionDto> cambiaEstadoReservacion(@Valid @RequestBody ReservacionDto nuevo, @PathVariable(name = "id")long id){
        if(!this.reservacionService.existePorId(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        ReservacionDto reservacionDtoRespuesta = reservacionService.actualizarReservacion(nuevo,id);
        return  new ResponseEntity<>(reservacionDtoRespuesta, HttpStatus.OK);
    }


}

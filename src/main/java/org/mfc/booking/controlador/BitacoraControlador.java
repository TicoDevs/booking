package org.mfc.booking.controlador;

import org.mfc.booking.dto.*;
import org.mfc.booking.servicio.BitacoraServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.mfc.booking.constantes.AppConstantes.*;
import static org.mfc.booking.constantes.AppConstantes.ORDENAR_DIRECCCION_DEFECTO;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/bitacora")
public class BitacoraControlador {
    @Autowired
    private BitacoraServicio bitacoraServicio;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listarBitacoraPageSort")
    public ResponseEntity<BitacoraContent> listarBitacoraContent(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_PAGINA_DEFECTO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_PAGINA_DEFECTO, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordernarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCCION_DEFECTO, required = false) String sortDir) {
        return new ResponseEntity(bitacoraServicio.listarPagSort(pageNo, pageSize, ordernarPor, sortDir), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<BitacoraDto> obtenerBitacoraPorId(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(bitacoraServicio.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public List<BitacoraDto> listar(){
        return bitacoraServicio.listar();
    }


}

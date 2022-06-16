package org.mfc.booking.controlador;

import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.dto.UsuarioRespuesta;
import org.mfc.booking.seguridad.dto.NuevoUsuario;
import org.mfc.booking.seguridad.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.mfc.booking.constantes.AppConstantes.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public UsuarioRespuesta listarUsuario(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_PAGINA_DEFECTO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_PAGINA_DEFECTO, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordernarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCCION_DEFECTO,required = false) String sortDir){
        return usuarioServicio.obtenerTodosLosUsuarios(pageNo, pageSize, ordernarPor, sortDir);
    }

    @GetMapping("/listar")
    public List<UsuarioDto> listar(){
        return usuarioServicio.listar();
    }

  @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorId(@PathVariable(name = "id")long id){
        return ResponseEntity.ok(usuarioServicio.obtenerUsuarioPorId(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(@RequestBody NuevoUsuario nuevoUsuario, @PathVariable(name = "id")long id){
        UsuarioDto usuarioRespuesta = usuarioServicio.actualizarUsuario(nuevoUsuario,id);
        return  new ResponseEntity<>(usuarioRespuesta, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable(name = "id")long id){
        usuarioServicio.eliminarUsuario(id);
        return new ResponseEntity<>("Usuario eliminado con exito", HttpStatus.OK);
    }
}

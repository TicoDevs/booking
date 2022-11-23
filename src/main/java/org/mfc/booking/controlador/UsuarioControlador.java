package org.mfc.booking.controlador;

import org.mfc.booking.dto.*;
import org.mfc.booking.seguridad.dto.NuevoUsuario;
import org.mfc.booking.seguridad.dto.RolDto;
import org.mfc.booking.seguridad.servicio.RolServicio;
import org.mfc.booking.seguridad.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.mfc.booking.constantes.AppConstantes.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private RolServicio rolServicio;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public UsuarioRespuesta listarUsuario(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_PAGINA_DEFECTO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_PAGINA_DEFECTO, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordernarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCCION_DEFECTO,required = false) String sortDir){
        return usuarioServicio.obtenerTodosLosUsuarios(pageNo, pageSize, ordernarPor, sortDir);
    }

    @GetMapping("/roles")
    public List<RolDto> getRoles(){
        return  rolServicio.listar();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public List<UsuarioDto> listar(){
        return usuarioServicio.listar();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscarId/{id}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorId(@PathVariable(name = "id")long id){
        return ResponseEntity.ok(usuarioServicio.obtenerUsuarioPorId(id));
    }

    @GetMapping("/buscarNombre/{nombreUsuOEmail}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorNombreUsuarioOEmail(@PathVariable(name = "nombreUsuOEmail")String userNameOEmail){
        return ResponseEntity.ok(usuarioServicio.obtenerPorNombreUsuarioOEmail(userNameOEmail));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(@Valid @RequestBody NuevoUsuario nuevoUsuario, @PathVariable(name = "id")long id){
        UsuarioDto usuarioRespuesta = usuarioServicio.actualizarUsuario(nuevoUsuario,id);
        return  new ResponseEntity<>(usuarioRespuesta, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable(name = "id")long id){
        usuarioServicio.eliminarUsuario(id);
        return new ResponseEntity(new Mensaje("Usuario eliminado con exito"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<UsuarioDto> registrar(@RequestBody NuevoUsuario nuevoUsuario) {
        if (usuarioServicio.existePorNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("Ya existe una cuenta con ese nombre de usuario"), HttpStatus.BAD_REQUEST);
        if (usuarioServicio.existePorEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("Ya existe una cuenta con ese email"), HttpStatus.BAD_REQUEST);
        UsuarioDto usuarioDto = usuarioServicio.registarUsuario(nuevoUsuario);
        return  new ResponseEntity<>(usuarioDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUX', 'GENE')")
    @PostMapping("/cambiarContrasena")
    public ResponseEntity<?> cambiarContrasena(@RequestBody CambioPassDto cambioPassDto){
        if (!usuarioServicio.existeNombreUsuarioOEmail(cambioPassDto.getUserNameOEmail(), cambioPassDto.getUserNameOEmail()))
            return new ResponseEntity(new Mensaje("No existe una cuenta con ese nombre de usuario"), HttpStatus.BAD_REQUEST);
        UsuarioDto usuarioDto = usuarioServicio.obtenerPorNombreUsuarioOEmail(cambioPassDto.getUserNameOEmail());
        NuevoUsuario nuevoUsuario = new NuevoUsuario();
        nuevoUsuario.setNombre(usuarioDto.getNombre());
        nuevoUsuario.setNombreUsuario(usuarioDto.getNombreUsuario());
        nuevoUsuario.setEmail(usuarioDto.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(cambioPassDto.getNewPass()));
        nuevoUsuario.setTelefono(usuarioDto.getTelefono());
        nuevoUsuario.setRoles(usuarioDto.getRoles());
        UsuarioDto usuarioDtoNew = usuarioServicio.actualizarUsuarioContraseña(nuevoUsuario, usuarioDto.getId());
        return new ResponseEntity(new Mensaje("Cambio con exito"), HttpStatus.CREATED);
    }


}

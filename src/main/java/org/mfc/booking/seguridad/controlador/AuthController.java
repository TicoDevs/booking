package org.mfc.booking.seguridad.controlador;
import org.mfc.booking.dto.Mensaje;
import org.mfc.booking.dto.ResetPassDto;
import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.entidad.ImageModel;
import org.mfc.booking.seguridad.dto.LoginUsuario;
import org.mfc.booking.seguridad.dto.NuevoUsuario;
import org.mfc.booking.seguridad.dto.UserInfoResponse;
import org.mfc.booking.seguridad.entidad.Rol;
import org.mfc.booking.seguridad.entidad.Usuario;
import org.mfc.booking.seguridad.enums.RolNombre;
import org.mfc.booking.seguridad.jwt.JwtProvider;
import org.mfc.booking.seguridad.repositorio.UsuarioRepositorio;
import org.mfc.booking.seguridad.servicio.RolServicio;
import org.mfc.booking.seguridad.servicio.UserDetailsImpl;
import org.mfc.booking.seguridad.servicio.UsuarioServicio;
import org.mfc.booking.servicio.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.mfc.booking.servicio.ProductoServicio;


import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mfc.booking.constantes.AppConstantes.NUMERO_PAGINA_DEFECTO;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    RolServicio rolServicio;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    ProductoServicio productoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        //return new ResponseEntity(new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities()), HttpStatus.OK);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody NuevoUsuario nuevoUsuario){
        if(usuarioRepositorio.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("Ya existe una cuenta con ese nombre de usuario"), HttpStatus.BAD_REQUEST);
        if(usuarioRepositorio.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("Ya existe una cuenta con ese email"), HttpStatus.BAD_REQUEST);
        Usuario usuario =
                new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_GENE).get());
        /*if(nuevoUsuario.getRoles().contains("admin") || nuevoUsuario.getRoles().contains("auxi")){
            roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_AUX).get());
        }
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_ADMIN).get());*/
        usuario.setRoles(roles);
        usuarioRepositorio.save(usuario);
        return new ResponseEntity(new Mensaje("Usuario guardado"), HttpStatus.CREATED);
    }

    @PostMapping("/validarOTP")
    public ResponseEntity<?> validarOtp(@RequestBody ResetPassDto resetPassDto){
        if (!usuarioServicio.existeNombreUsuarioOEmail(resetPassDto.getUserNameOEmail(), resetPassDto.getUserNameOEmail()))
            return new ResponseEntity(new Mensaje("No existe una cuenta con ese nombre de usuario"), HttpStatus.BAD_REQUEST);
        UsuarioDto usuarioDto = usuarioServicio.obtenerPorNombreUsuarioOEmail(resetPassDto.getUserNameOEmail());
        if (usuarioDto.getPassword().equals(resetPassDto.getOtp())){
            NuevoUsuario nuevoUsuario = new NuevoUsuario();
            nuevoUsuario.setNombre(usuarioDto.getNombre());
            nuevoUsuario.setNombreUsuario(usuarioDto.getNombreUsuario());
            nuevoUsuario.setEmail(usuarioDto.getEmail());
            nuevoUsuario.setPassword(passwordEncoder.encode(resetPassDto.getNewPass()));
            nuevoUsuario.setRoles(usuarioDto.getRoles());
            UsuarioDto usuarioDtoNew = usuarioServicio.actualizarUsuarioContraseña(nuevoUsuario, usuarioDto.getId());
            return new ResponseEntity(new Mensaje("Cambio de contraseña con exito"), HttpStatus.CREATED);
        }else {
            return new ResponseEntity(new Mensaje("OTP no coincide"), HttpStatus.BAD_REQUEST);
        }
    }

   @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtProvider.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new Mensaje("Has sido desconectado!"));
    }

    @GetMapping("/listarImagenes")
    public ResponseEntity<Set<ImageModel>> listarImagenes() {
        Set<ImageModel> list = productoServicio.listar().stream()
                .flatMap(c -> c.getImagenes()
                        .stream())
                .collect(Collectors.toSet());
        return new ResponseEntity(list, HttpStatus.OK);
    }

}

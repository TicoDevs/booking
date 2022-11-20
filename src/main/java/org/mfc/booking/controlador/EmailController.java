package org.mfc.booking.controlador;

import org.mfc.booking.dto.EmailMessage;
import org.mfc.booking.dto.Mensaje;
import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.seguridad.dto.NuevoUsuario;
import org.mfc.booking.seguridad.servicio.RolServicio;
import org.mfc.booking.seguridad.servicio.UsuarioServicio;
import org.mfc.booking.servicio.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private RolServicio rolServicio;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private  EmailSenderService emailSenderService;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage) {

        this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/buscarNombre/{nombreUsuOEmail}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorNombreUsuarioOEmail(@PathVariable(name = "nombreUsuOEmail")String userNameOEmail){
        return ResponseEntity.ok(usuarioServicio.obtenerPorNombreUsuarioOEmail(userNameOEmail));
    }

    @PostMapping("/send-reset-password/{nombreUsuOEmail}")
    public ResponseEntity<?> sendResetPassword(@PathVariable(name = "nombreUsuOEmail")String userNameOEmail) {
        if (!usuarioServicio.existeNombreUsuarioOEmail(userNameOEmail, userNameOEmail))
            return new ResponseEntity(new Mensaje("No existe una cuenta con ese nombre de usuario"), HttpStatus.BAD_REQUEST);
        UsuarioDto usuarioDto = usuarioServicio.obtenerPorNombreUsuarioOEmail(userNameOEmail);
        String tokenGenerico = UUID.randomUUID().toString();
        NuevoUsuario nuevoUsuario = new NuevoUsuario();
        nuevoUsuario.setNombre(usuarioDto.getNombre());
        nuevoUsuario.setNombreUsuario(usuarioDto.getNombreUsuario());
        nuevoUsuario.setEmail(usuarioDto.getEmail());
        nuevoUsuario.setPassword(tokenGenerico);
        nuevoUsuario.setRoles(usuarioDto.getRoles());
        UsuarioDto usuarioDtoNew = usuarioServicio.actualizarUsuarioContraseña(nuevoUsuario, usuarioDto.getId());
        this.emailSenderService.sendEmailToUsers(usuarioDto.getEmail(),"Cambio de contraseña MFC",usuarioDto.getNombre(), tokenGenerico);
        return new ResponseEntity<>(new Mensaje("Favor verificar su correo"), HttpStatus.OK);
    }


}

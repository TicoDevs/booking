package org.mfc.booking.util;

import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.seguridad.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Utilidades {

    @Autowired
    UsuarioServicio usuarioServicio;

    public UsuarioDto obtenerUsuarioActual(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return this.usuarioServicio.obtenerPorNombreUsuario(login);
    }

}

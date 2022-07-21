package org.mfc.booking.seguridad.dto;
import lombok.*;
import org.mfc.booking.seguridad.entidad.Rol;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NuevoUsuario {

    private String nombre;
    private String nombreUsuario;
    private String email;
    private String password;
    private Set<Rol> roles = new HashSet<>();
}
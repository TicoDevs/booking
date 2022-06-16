package org.mfc.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mfc.booking.seguridad.entidad.Rol;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UsuarioDto {

    private long id;
    private String nombre;
    private String nombreUsuario;
    private String email;
    private String password;
    private Set<Rol> roles = new HashSet<>();
}

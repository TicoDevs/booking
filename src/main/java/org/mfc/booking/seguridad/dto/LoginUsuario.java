package org.mfc.booking.seguridad.dto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUsuario {
    @NotBlank
    private String nombreUsuario;
    @NotBlank
    private String password;
}

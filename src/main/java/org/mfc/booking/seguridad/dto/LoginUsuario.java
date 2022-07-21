package org.mfc.booking.seguridad.dto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginUsuario {

    @NotBlank(message = "No en blanco")
    private String nombreUsuario;
    @NotBlank(message = "No en blanco")
    private String password;
}

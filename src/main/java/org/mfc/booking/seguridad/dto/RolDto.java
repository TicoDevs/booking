package org.mfc.booking.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mfc.booking.seguridad.enums.RolNombre;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolDto {

    private long id;
    private RolNombre rolNombre;
}

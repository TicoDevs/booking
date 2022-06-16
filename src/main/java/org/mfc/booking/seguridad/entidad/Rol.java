package org.mfc.booking.seguridad.entidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mfc.booking.seguridad.enums.RolNombre;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;
}

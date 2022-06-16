package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.seguridad.entidad.Rol;
import org.mfc.booking.seguridad.enums.RolNombre;

import java.util.Optional;

public interface RolServicio {
    public Optional<Rol> getByRolNombre(RolNombre rolNombre);
    public void save(Rol rol);
}

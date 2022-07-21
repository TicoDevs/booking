package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.seguridad.dto.RolDto;
import org.mfc.booking.seguridad.entidad.Rol;
import org.mfc.booking.seguridad.enums.RolNombre;

import java.util.List;
import java.util.Optional;

public interface RolServicio {
    public List<RolDto> listar();
    public Optional<Rol> getByRolNombre(RolNombre rolNombre);
    public void save(Rol rol);
}

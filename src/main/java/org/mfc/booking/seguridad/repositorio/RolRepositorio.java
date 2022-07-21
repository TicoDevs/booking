package org.mfc.booking.seguridad.repositorio;


import org.mfc.booking.seguridad.entidad.Rol;
import org.mfc.booking.seguridad.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepositorio extends JpaRepository<Rol, Integer>{
    public Optional<Rol> findByRolNombre(RolNombre rolNombre);
}

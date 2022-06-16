package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.seguridad.entidad.Rol;
import org.mfc.booking.seguridad.enums.RolNombre;
import org.mfc.booking.seguridad.repositorio.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RolServicioImpl implements RolServicio{
    @Autowired
    RolRepositorio rolRepositorio;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepositorio.findByRolNombre(rolNombre);
    }

    public void save(Rol rol){
        rolRepositorio.save(rol);
    }
}

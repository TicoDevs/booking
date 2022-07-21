package org.mfc.booking.repositorio;


import org.mfc.booking.entidad.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BitacoraRepositorio extends JpaRepository<Bitacora,Long> {

    Optional<Bitacora> findByIdUsuarioMiembro(Long idUsuarioMiembro);
    Optional<Bitacora> findByIdProducto(Long idProducto);

}

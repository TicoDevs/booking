package org.mfc.booking.repositorio;

import org.mfc.booking.entidad.Producto;
import org.mfc.booking.entidad.Reservacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservacionRepositorio extends JpaRepository<Reservacion,Long> {

    Optional<Reservacion> findById(long id);
    boolean existsById(long id);

}

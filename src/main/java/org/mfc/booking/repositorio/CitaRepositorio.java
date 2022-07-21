package org.mfc.booking.repositorio;


import org.mfc.booking.entidad.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CitaRepositorio extends JpaRepository<Cita,Long> {

}

package org.mfc.booking.repositorio;

import org.mfc.booking.entidad.DetalleReservacionProd;
import org.mfc.booking.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetalleReservacionProdRepositorio extends JpaRepository<DetalleReservacionProd,Long> {
}

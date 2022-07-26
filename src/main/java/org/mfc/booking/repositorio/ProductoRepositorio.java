package org.mfc.booking.repositorio;


import org.mfc.booking.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepositorio extends JpaRepository<Producto,Long> {
    Optional<Producto> findByNombre(String nombre);
    Optional<Producto> findByCodigo(String codigo);
    boolean existsByNombre(String nombre);
}

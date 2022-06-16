package org.mfc.booking.seguridad.repositorio;

import org.mfc.booking.seguridad.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByNombreUsuarioOrEmail(String username, String email);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);

}

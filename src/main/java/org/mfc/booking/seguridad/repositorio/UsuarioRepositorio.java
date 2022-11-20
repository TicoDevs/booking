package org.mfc.booking.seguridad.repositorio;

import org.mfc.booking.seguridad.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    public Optional<Usuario> findByEmail(String email);
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    public Optional<Usuario> findByNombreUsuarioOrEmail(String username, String email);
    public boolean existsByNombreUsuario(String nombreUsuario);
    public boolean existsByEmail(String email);

    public boolean existsByNombreUsuarioOrEmail(String username, String email);

}

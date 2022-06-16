package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.dto.UsuarioRespuesta;
import org.mfc.booking.seguridad.dto.NuevoUsuario;
import org.mfc.booking.seguridad.entidad.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioServicio {
    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario);
    public boolean existePorNombreUsuario(String nombreUsuario);
    public boolean existePorEmail(String email);
    public void crear(Usuario usuario);
    public List<UsuarioDto> listar();
    public UsuarioRespuesta obtenerTodosLosUsuarios(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public UsuarioDto obtenerUsuarioPorId(long id);
    public UsuarioDto actualizarUsuario(NuevoUsuario nuevoUsuario, long id);
    public void eliminarUsuario(long id);

}

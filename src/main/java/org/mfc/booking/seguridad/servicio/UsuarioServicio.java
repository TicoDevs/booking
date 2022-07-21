package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.dto.UsuarioRespuesta;
import org.mfc.booking.seguridad.dto.NuevoUsuario;
import org.mfc.booking.seguridad.entidad.Usuario;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioServicio {

    public UsuarioDto registarUsuario(NuevoUsuario nuevoUsuario);
    public List<UsuarioDto> listar();
    public UsuarioRespuesta obtenerTodosLosUsuarios(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public UsuarioDto obtenerPorNombreUsuario(String nombreUsuario);
    public UsuarioDto obtenerPorNombreUsuarioOEmail(String nombreUsuario);
    public UsuarioDto obtenerUsuarioPorId(long id);
    public boolean existePorNombreUsuario(String nombreUsuario);
    public boolean existePorEmail(String email);
    public UsuarioDto actualizarUsuario(NuevoUsuario nuevoUsuario, long id);
    public void eliminarUsuario(long id);

}

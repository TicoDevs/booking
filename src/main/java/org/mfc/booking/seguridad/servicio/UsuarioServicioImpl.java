package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.dto.Mensaje;
import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.dto.UsuarioRespuesta;
import org.mfc.booking.excepcion.ResourceNotFoundException;
import org.mfc.booking.seguridad.dto.NuevoUsuario;
import org.mfc.booking.seguridad.entidad.Rol;
import org.mfc.booking.seguridad.entidad.Usuario;
import org.mfc.booking.seguridad.enums.RolNombre;
import org.mfc.booking.seguridad.repositorio.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mfc.booking.seguridad.enums.RolNombre.*;

@Service
@Transactional
public class UsuarioServicioImpl implements UsuarioServicio{

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    RolServicio rolServicio;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UsuarioDto registarUsuario(NuevoUsuario nuevoUsuario) {
        Usuario usuario =
                new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<Rol> roles = new HashSet<>();
        for( Rol rol : nuevoUsuario.getRoles()) {
            if (rol.getRolNombre().equals(RolNombre.ROLE_GENE)) {
                roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_GENE).get());
            }
            if (rol.getRolNombre().equals(RolNombre.ROLE_AUX)) {
                roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_AUX).get());
            }
            if (rol.getRolNombre().equals(RolNombre.ROLE_ADMIN))
                roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        }
        usuario.setRoles(roles);
        Usuario usuarioNuevo = usuarioRepositorio.save(usuario);
        return mappearDTO(usuarioNuevo);
    }

    @Override
    public List<UsuarioDto> listar() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        List<UsuarioDto> usuarioDtos = usuarios.stream().map(usuario -> mappearDTO(usuario)).collect(Collectors.toList());
        return usuarioDtos;
    }

    @Override
    public UsuarioRespuesta obtenerTodosLosUsuarios(int pageNo, int pageSize, String ordernarPor, String sortDir) {
        Sort sort  = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(ordernarPor).ascending():Sort.by(ordernarPor).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Usuario> usuarioPage = usuarioRepositorio.findAll(pageable);
        List<Usuario> ListUsuarios = usuarioPage.getContent();
        List<UsuarioDto> contenido = ListUsuarios.stream().map(usuario -> mappearDTO(usuario))
                .collect(Collectors.toList());
        UsuarioRespuesta usuarioRespuesta = new UsuarioRespuesta();
        usuarioRespuesta.setContenido(contenido);
        usuarioRespuesta.setNumeroPaginas(usuarioPage.getNumber());
        usuarioRespuesta.setMedidaPagina(usuarioPage.getSize());
        usuarioRespuesta.setTotalPaginas(usuarioPage.getTotalPages());
        usuarioRespuesta.setTotalElementos(usuarioPage.getTotalElements());
        usuarioRespuesta.setUltima(usuarioPage.isLast());
        return usuarioRespuesta;
    }

    @Override
    public UsuarioDto obtenerUsuarioPorId(long id) {
        Usuario usuario =  usuarioRepositorio.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Usuario","Id", String.valueOf(id)));
        Usuario nuevoUsu = new Usuario();

        return mappearDTO(usuario);
    }

    @Override
    public UsuarioDto obtenerPorNombreUsuario(String nombreUsuario){
        Usuario usuario = usuarioRepositorio.findByNombreUsuario(nombreUsuario).orElseThrow(()-> new ResourceNotFoundException("Usuario","nombre usuario",nombreUsuario));
        return  mappearDTO(usuario);
    }

    @Override
    public UsuarioDto obtenerPorNombreUsuarioOEmail(String nombreUsuario) {
        Usuario usuario = usuarioRepositorio.findByNombreUsuarioOrEmail(nombreUsuario, nombreUsuario).orElseThrow(()-> new ResourceNotFoundException("Usuario","nombre usuario",nombreUsuario));
        return  mappearDTO(usuario);
    }

    @Override
    public boolean existePorNombreUsuario(String nombreUsuario){
        return usuarioRepositorio.existsByNombreUsuario(nombreUsuario);
    }

    @Override
    public boolean existePorEmail(String email){
        return usuarioRepositorio.existsByEmail(email);
    }


    @Override
    public UsuarioDto actualizarUsuario(NuevoUsuario nuevoUsuario, long id) {
        Set<Rol> roles = new HashSet<>();
        Usuario usuario =  usuarioRepositorio.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Usuario","Id", String.valueOf(id)));
        usuario.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        usuario.setNombre(nuevoUsuario.getNombre());
        usuario.setEmail(nuevoUsuario.getEmail());
        for( Rol rol : nuevoUsuario.getRoles()) {
            if (rol.getRolNombre().equals(RolNombre.ROLE_GENE)) {
                roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_GENE).get());
            }
            if (rol.getRolNombre().equals(RolNombre.ROLE_AUX)) {
                roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_AUX).get());
            }
            if (rol.getRolNombre().equals(RolNombre.ROLE_ADMIN))
                roles.add(rolServicio.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        }
        usuario.setRoles(roles);
        Usuario usuarioAct = usuarioRepositorio.save(usuario);
        return mappearDTO(usuarioAct);
    }

    @Override
    public void eliminarUsuario(long id) {
        Usuario usuario =  usuarioRepositorio.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Usuario","Id", String.valueOf(id)));
        usuarioRepositorio.delete(usuario);
    }

    //Convierte entidad a DTO
    private UsuarioDto mappearDTO (Usuario usuario){
        UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
        return usuarioDto;
    }

    //Convierte DTO a entidad
    private Usuario mappearEntidad (UsuarioDto usuarioDto){
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);
        return usuario;
    }
}

package org.mfc.booking.seguridad.servicio;

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

    @Override
    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario){
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario);
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
    public void crear(Usuario usuario){
        usuarioRepositorio.save(usuario);
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
                .orElseThrow(() ->new ResourceNotFoundException("Usuario","Id", id));
        return mappearDTO(usuario);
    }

    @Override
    public UsuarioDto actualizarUsuario(NuevoUsuario nuevoUsuario, long id) {
        Set<Rol> roles = new HashSet<>();
        Usuario usuario =  usuarioRepositorio.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Usuario","Id", id));
        usuario.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        usuario.setNombre(nuevoUsuario.getNombre());
        usuario.setEmail(nuevoUsuario.getEmail());
        if(nuevoUsuario.getRoles().contains("gene")) {
            roles.add(rolServicio.getByRolNombre(ROLE_GENE).get());
        }
        if(nuevoUsuario.getRoles().contains("auxi")){
            roles.add(rolServicio.getByRolNombre(ROLE_AUX).get());
        }
        if(nuevoUsuario.getRoles().contains("admin")) {
            roles.add(rolServicio.getByRolNombre(ROLE_ADMIN).get());
        }
        usuario.setRoles(roles);
        Usuario usuarioAct = usuarioRepositorio.save(usuario);
        return mappearDTO(usuarioAct);
    }

    @Override
    public void eliminarUsuario(long id) {
        Usuario usuario =  usuarioRepositorio.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Usuario","Id", id));
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

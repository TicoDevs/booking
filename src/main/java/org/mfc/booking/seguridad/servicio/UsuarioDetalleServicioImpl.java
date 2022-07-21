package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.seguridad.entidad.Usuario;
import org.mfc.booking.seguridad.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;


@Service
public class UsuarioDetalleServicioImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByNombreUsuarioOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ese username o email :" + usernameOrEmail));
        return  UserDetailsImpl.build(usuario);
    }

   /* private Collection<? extends GrantedAuthority> mapearRoles(Set<Rol> roles) {
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getRolNombre().toString())).collect(Collectors.toList());
    }*/
}
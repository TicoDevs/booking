package org.mfc.booking.seguridad.servicio;

import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.seguridad.dto.RolDto;
import org.mfc.booking.seguridad.entidad.Rol;
import org.mfc.booking.seguridad.entidad.Usuario;
import org.mfc.booking.seguridad.enums.RolNombre;
import org.mfc.booking.seguridad.repositorio.RolRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolServicioImpl implements RolServicio{
    @Autowired
    RolRepositorio rolRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<RolDto> listar() {
        List<Rol> roles = rolRepositorio.findAll();
        List<RolDto> rolDtos = roles.stream().map(rol -> mappearDTO(rol)).collect(Collectors.toList());
        return rolDtos;
    }

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepositorio.findByRolNombre(rolNombre);
    }

    public void save(Rol rol){
        rolRepositorio.save(rol);
    }

    //Convierte entidad a DTO
    private RolDto mappearDTO (Rol rol){
        RolDto rolDto = modelMapper.map(rol, RolDto.class);
        return rolDto;
    }

    //Convierte DTO a entidad
    private Rol mappearEntidad (RolDto rolDto){
        Rol rol = modelMapper.map(rolDto, Rol.class);
        return rol;
    }
}

package org.mfc.booking.servicio;

import org.mfc.booking.dto.BitacoraContent;
import org.mfc.booking.dto.BitacoraDto;

import org.mfc.booking.dto.ProductoContent;
import org.mfc.booking.dto.ProductoDto;
import org.mfc.booking.entidad.Bitacora;
import org.mfc.booking.entidad.Producto;
import org.mfc.booking.excepcion.ResourceNotFoundException;
import org.mfc.booking.repositorio.BitacoraRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BitacoraServicioImpl implements BitacoraServicio{

    @Autowired
    BitacoraRepositorio bitacoraRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BitacoraDto crearBitacora(BitacoraDto bitacoraDto) {
        //Convertimos de DTO a entidad
        Bitacora bitacora = mappearEntidad(bitacoraDto);
        Bitacora nueva = bitacoraRepositorio.save(bitacora);
        BitacoraDto bitacoraDtoRespuesta = mappearDTO(nueva);
        return bitacoraDtoRespuesta;
    }

    @Override
    public BitacoraDto actualizarBitacora(BitacoraDto bitacoraDto, long id) {
        Bitacora bitacora = bitacoraRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bitacora", "Id", String.valueOf(id)));
        bitacora.setIdUsuario(bitacoraDto.getIdUsuario());
        bitacora.setIdUsuarioMiembro(bitacoraDto.getIdUsuarioMiembro());
        bitacora.setFechaIngreso(bitacoraDto.getFechaIngreso());
        bitacora.setUltimaModificacion(bitacoraDto.getUltimaModificacion());
        bitacora.setFechaSalida(bitacoraDto.getFechaSalida());
        bitacora.setDescripcion(bitacoraDto.getDescripcion());
        bitacora.setIdProducto(bitacoraDto.getIdProducto());
        bitacora.setEstado(bitacoraDto.getEstado());
        Bitacora nueva = bitacoraRepositorio.save(bitacora);
        BitacoraDto bitacoraDtoRespuesta = mappearDTO(nueva);
        return bitacoraDtoRespuesta;
    }

    @Override
    public List<BitacoraDto> listar() {
        List<Bitacora> bitacoraList = bitacoraRepositorio.findAll();
        List<BitacoraDto> bitacoraDtoList = bitacoraList.stream().map(bitacora -> mappearDTO(bitacora)).collect(Collectors.toList());
        return bitacoraDtoList;
    }

    @Override
    public BitacoraContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir) {
        Sort sort  = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(ordernarPor).ascending():Sort.by(ordernarPor).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Bitacora> bitacoras = bitacoraRepositorio.findAll(pageable);
        List<Bitacora> bitacoraList = bitacoras.getContent();
        List<BitacoraDto> contenido = bitacoraList.stream().map(bitacora -> mappearDTO(bitacora))
                .collect(Collectors.toList());
        BitacoraContent bitacoraContent = new BitacoraContent();
        bitacoraContent.setNumeroPaginas(bitacoras.getNumber());
        bitacoraContent.setMedidaPagina(bitacoras.getSize());
        bitacoraContent.setTotalElementos(bitacoras.getTotalElements());
        bitacoraContent.setTotalPaginas(bitacoras.getTotalPages());
        bitacoraContent.setUltima(bitacoras.isLast());
        bitacoraContent.setContenido(contenido);
        return bitacoraContent;
    }

    @Override
    public BitacoraDto getById(Long id) {
        Bitacora bitacora = bitacoraRepositorio.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Bitacora","Id", String.valueOf(id)));
        return mappearDTO(bitacora);
    }

    @Override
    public BitacoraDto getByIdProducto(Long id) {
        Bitacora bitacora = bitacoraRepositorio.findByIdProducto(id)
                .orElseThrow(() ->new ResourceNotFoundException("Bitacora producto","Id", String.valueOf(id)));
        return mappearDTO(bitacora);
    }

    @Override
    public BitacoraDto getByIdMiembro(Long id) {
        Bitacora bitacora = bitacoraRepositorio.findByIdProducto(id)
                .orElseThrow(() ->new ResourceNotFoundException("Bitacora miembro","Id", String.valueOf(id)));
        return mappearDTO(bitacora);
    }

    @Override
    public BitacoraDto cambiarEstado(Long id) {
        Bitacora bitacora = bitacoraRepositorio.findByIdProducto(id)
                .orElseThrow(() ->new ResourceNotFoundException("Bitacora","Id", String.valueOf(id)));
        bitacora.setEstado(Boolean.FALSE);
        Bitacora nueva = bitacoraRepositorio.save(bitacora);
        BitacoraDto bitacoraDtoRespuesta = mappearDTO(nueva);
        return bitacoraDtoRespuesta;
    }

    //Convierte entidad a DTO
    private BitacoraDto mappearDTO (Bitacora bitacora){
        BitacoraDto bitacoraDto = modelMapper.map(bitacora, BitacoraDto.class);
        return bitacoraDto;
    }

    //Convierte DTO a entidad
    private Bitacora mappearEntidad (BitacoraDto bitacoraDto){
        Bitacora bitacora = modelMapper.map(bitacoraDto, Bitacora.class);
        return bitacora;
    }
}

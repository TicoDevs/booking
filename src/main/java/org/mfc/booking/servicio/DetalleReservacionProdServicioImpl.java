package org.mfc.booking.servicio;

import org.mfc.booking.dto.BitacoraDto;
import org.mfc.booking.dto.DetalleReservacionProdDto;
import org.mfc.booking.dto.ProductoDto;
import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.entidad.DetalleReservacionProd;
import org.mfc.booking.entidad.Producto;
import org.mfc.booking.excepcion.ResourceNotFoundException;
import org.mfc.booking.repositorio.DetalleReservacionProdRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DetalleReservacionProdServicioImpl implements DetalleReservacionProdServicio{

    @Autowired
    DetalleReservacionProdRepositorio detalleReservacionProdRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<DetalleReservacionProdDto> listar() {
        List<DetalleReservacionProd> detalleReservacionProdListar = detalleReservacionProdRepositorio.findAll();
        List<DetalleReservacionProdDto> detalleReservacionProdListarDto = detalleReservacionProdListar.stream().map(usuario -> mappearDTO(usuario)).collect(Collectors.toList());
        return detalleReservacionProdListarDto;
    }

    @Override
    public DetalleReservacionProdDto obtenerDetalleReservacionProdId(Long Id) {
        DetalleReservacionProd detalleReservacionProd =  detalleReservacionProdRepositorio.findById(Id)
                .orElseThrow(() ->new ResourceNotFoundException("Detalle","Id", String.valueOf(Id)));
        return mappearDTO(detalleReservacionProd);
    }

    @Override
    public boolean existePorId(long id) {
        return detalleReservacionProdRepositorio.existsById(id);
    }

    @Override
    public DetalleReservacionProdDto crear(DetalleReservacionProdDto detalleReservacionProdDto) {
        DetalleReservacionProd detalleReservacionProd = mappearEntidad(detalleReservacionProdDto);
        DetalleReservacionProd nueva = detalleReservacionProdRepositorio.save(detalleReservacionProd);
        DetalleReservacionProdDto detalleReservacionProdDtoRespuesta = mappearDTO(nueva);
        return detalleReservacionProdDtoRespuesta;
    }

    @Override
    public void eliminarDetalleReservacionProd(long id) {
        DetalleReservacionProd detalleReservacionProd = detalleReservacionProdRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "Id", String.valueOf(id)));
        detalleReservacionProdRepositorio.delete(detalleReservacionProd);
    }


    //Convierte entidad a DTO
    private DetalleReservacionProdDto mappearDTO (DetalleReservacionProd detalleReservacionProd){
        DetalleReservacionProdDto detalleReservacionProdDto = modelMapper.map(detalleReservacionProd, DetalleReservacionProdDto.class);
        return detalleReservacionProdDto;
    }

    //Convierte DTO a entidad
    private DetalleReservacionProd mappearEntidad (DetalleReservacionProdDto detalleReservacionProdDto){
        DetalleReservacionProd detalleReservacionProd = modelMapper.map(detalleReservacionProdDto, DetalleReservacionProd.class);
        return detalleReservacionProd;
    }
}

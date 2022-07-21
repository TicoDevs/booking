package org.mfc.booking.servicio;

import org.mfc.booking.dto.BitacoraDto;
import org.mfc.booking.dto.ProductoContent;
import org.mfc.booking.dto.ProductoDto;
import org.mfc.booking.entidad.Producto;
import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.excepcion.ResourceNotFoundException;
import org.mfc.booking.repositorio.ProductoRepositorio;
import org.mfc.booking.util.Utilidades;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoServicioImpl implements ProductoServicio {

    @Autowired
    ProductoRepositorio productoRepositorio;
    @Autowired
    BitacoraServicio bitacoraServicio;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Utilidades util;



    @Override
    public List<ProductoDto> listar() {
        List<Producto> productoList = productoRepositorio.findAll();
        List<ProductoDto> productoDtoList = productoList.stream().map(usuario -> mappearDTO(usuario)).collect(Collectors.toList());
        return productoDtoList;
    }

    @Override
    public ProductoContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir) {
        Sort sort  = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(ordernarPor).ascending():Sort.by(ordernarPor).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Producto> productos = productoRepositorio.findAll(pageable);
        List<Producto> productoList = productos.getContent();
        List<ProductoDto> contenido = productoList.stream().map(producto -> mappearDTO(producto))
                .collect(Collectors.toList());
        ProductoContent productoContent = new ProductoContent();
        productoContent.setNumeroPaginas(productos.getNumber());
        productoContent.setMedidaPagina(productos.getSize());
        productoContent.setTotalElementos(productos.getTotalElements());
        productoContent.setTotalPaginas(productos.getTotalPages());
        productoContent.setUltima(productos.isLast());
        productoContent.setContenido(contenido);
        return productoContent;
    }

    @Override
    public ProductoDto obtenerProductoPorId(Long id) {
        Producto producto =  productoRepositorio.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Producto","Id", String.valueOf(id)));
        return mappearDTO(producto);
    }

    @Override
    public ProductoDto obtenerProductoPorCodigo(String codigo) {
        Producto producto =  productoRepositorio.findByCodigo(codigo)
                .orElseThrow(() ->new ResourceNotFoundException("Producto","Codigo", codigo));
        return mappearDTO(producto);
    }

    @Override
    public boolean existePorId(long id) {
        return productoRepositorio.existsById(id);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return productoRepositorio.existsByNombre(nombre);
    }

    @Override
    public ProductoDto crear(ProductoDto productoDto) {
        //Convertimos de DTO a entidad
        Producto producto = mappearEntidad(productoDto);
        Producto nueva = productoRepositorio.save(producto);
        UsuarioDto usu = this.util.obtenerUsuarioActual();
        BitacoraDto bit = new BitacoraDto();
        bit.setIdUsuario(usu.getId());
        bit.setIdProducto(nueva.getId());
        bit.setFechaIngreso(new Date());
        bit.setDescripcion("Libro: " + nueva.getNombre() + " ISBN:" + nueva.getCodigo() + " Proceso: Ingresó" + " /Usuario: " + usu.getNombre());
        bit.setEstado(Boolean.TRUE);
        bitacoraServicio.crearBitacora(bit);
        //Convertimos de entidad a DTO
        ProductoDto productoDtoRepuesta = mappearDTO(nueva);
        return productoDtoRepuesta;
    }

    @Override
    public ProductoDto actualizarProducto(ProductoDto productoDto, long id) {
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "Id", String.valueOf(id)));
        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setCantidad(productoDto.getCantidad());
        producto.setEstado(productoDto.getEstado());
        Set<String> imagenes = new HashSet<String>();
        if (!imagenes.isEmpty()){
            imagenes.addAll(productoDto.getImg());
            producto.setImg(imagenes.stream().collect(Collectors.toSet()));
        }
        producto.setCodigo(productoDto.getCodigo());
        Producto productoActualizada = productoRepositorio.save(producto);
        UsuarioDto usu = this.util.obtenerUsuarioActual();
        BitacoraDto bit = this.bitacoraServicio.getByIdProducto(productoActualizada.getId());
        bit.setIdUsuario(usu.getId());
        bit.setUltimaModificacion(new Date());
        bit.setDescripcion("Libro: " + productoActualizada.getNombre() + " ISBN:" + productoActualizada.getCodigo() + " Proceso: Actualizó" + " /Usuario: " + usu.getNombre());
        bitacoraServicio.actualizarBitacora(bit, bit.getId());
        return mappearDTO(productoActualizada);

    }

    @Override
    public void eliminarProducto(long id) {
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "Id", String.valueOf(id)));
        productoRepositorio.delete(producto);
    }

    //Convierte entidad a DTO
    private ProductoDto mappearDTO (Producto producto){
        ProductoDto productoDto = modelMapper.map(producto, ProductoDto.class);
        return productoDto;
    }

    //Convierte DTO a entidad
    private Producto mappearEntidad (ProductoDto productoDto){
        Producto producto = modelMapper.map(productoDto, Producto.class);
        return producto;
    }

}

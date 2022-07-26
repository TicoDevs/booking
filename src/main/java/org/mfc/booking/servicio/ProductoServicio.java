package org.mfc.booking.servicio;

import org.mfc.booking.dto.ProductoContent;
import org.mfc.booking.dto.ProductoDto;

import java.util.List;

public interface ProductoServicio {

    public List<ProductoDto> listar();
    public ProductoContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public ProductoDto obtenerProductoPorId(Long Id);
    public ProductoDto obtenerProductoPorCodigo(String codigo);
    public boolean existePorId(long id);
    public boolean existePorNombre(String nombre);
    public ProductoDto crear(ProductoDto productoDto);
    public ProductoDto actualizarProducto(ProductoDto productoDto, long id);
    public ProductoDto actualizarProductoReserv(ProductoDto productoDto, long id);
    public ProductoDto actProdCambioEstado(ProductoDto productoDto, long id);
    public void eliminarProducto(long id);

}

package org.mfc.booking.servicio;

import org.mfc.booking.dto.BitacoraContent;
import org.mfc.booking.dto.BitacoraDto;
import org.mfc.booking.dto.ProductoContent;

import java.util.List;

public interface BitacoraServicio {

    public BitacoraDto crearBitacora(BitacoraDto bitacoraDto);
    public BitacoraDto actualizarBitacora(BitacoraDto bitacoraDto, long id);
    public List<BitacoraDto> listar();
    public BitacoraContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public BitacoraDto getById(Long id);
    public BitacoraDto getByIdProducto(Long id);
    public BitacoraDto getByIdMiembro(Long id);
    public BitacoraDto cambiarEstado(Long id);

}

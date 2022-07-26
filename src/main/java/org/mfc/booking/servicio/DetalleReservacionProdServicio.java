package org.mfc.booking.servicio;


import org.mfc.booking.dto.DetalleReservacionProdDto;
import org.mfc.booking.entidad.DetalleReservacionProd;

import java.util.List;

public interface DetalleReservacionProdServicio {

    public List<DetalleReservacionProdDto> listar();
    public DetalleReservacionProdDto obtenerDetalleReservacionProdId(Long Id);
    public boolean existePorId(long id);
    public DetalleReservacionProdDto crear(DetalleReservacionProdDto detalleReservacionProdDto);
    public void eliminarDetalleReservacionProd(long id);

}

package org.mfc.booking.servicio;

import org.mfc.booking.dto.ReservacionContent;
import org.mfc.booking.dto.ReservacionDto;

import java.util.List;

public interface ReservacionServicio {


    public List<ReservacionDto> listar();
    public ReservacionContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public ReservacionContent listarProdPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public ReservacionContent listarCitaPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public ReservacionDto obtenerReservacionPorId(Long Id);
    public boolean existePorId(long id);
    public ReservacionDto crearReservProd(ReservacionDto reservacionDto);
    public ReservacionDto crearReservCita(ReservacionDto reservacionDto);
    public ReservacionDto actualizarReservacion(ReservacionDto reservacionDto, long id);
    public void eliminarReservacion(long id);
}

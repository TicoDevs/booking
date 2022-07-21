package org.mfc.booking.servicio;

import org.mfc.booking.dto.CitaContent;
import org.mfc.booking.dto.CitaDto;

import java.util.List;

public interface CitaServicio {

    public List<CitaDto> listar();
    public CitaContent listarPagSort(int pageNo, int pageSize, String ordernarPor, String sortDir);
    public CitaDto obtenerCitaPorId(Long Id);
    public boolean existePorId(long id);
    public CitaDto crear( CitaDto citaDto);
    public CitaDto actualizarCita( CitaDto citaDto, long id);
    public void eliminarCita(long id);

}

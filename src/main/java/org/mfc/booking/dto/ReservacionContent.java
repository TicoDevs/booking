package org.mfc.booking.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservacionContent {

    private List<ReservacionDto> contenido;
    private int numeroPaginas;
    private int medidaPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean ultima;

}

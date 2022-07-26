package org.mfc.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mfc.booking.entidad.Reservacion;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleReservacionProdDto {
        private long id;
        private Integer cantidad;
        private ProductoDto producto;
}

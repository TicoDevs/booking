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
        private long idProducto;
        private String codigo;
        private Integer cantidad;
        private Reservacion reservacion;

}

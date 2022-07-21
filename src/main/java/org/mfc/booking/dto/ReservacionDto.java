package org.mfc.booking.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mfc.booking.entidad.DetalleReservacionProd;
import org.mfc.booking.entidad.Producto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ReservacionDto {

    private long id;
    private Date fechaReservacion;
    private Boolean estado;
    private Integer tipo;
    private long idMiembro;
    private List<DetalleReservacionProd> prodSet;
    private long idCita;
    private String detalleReserva;

}

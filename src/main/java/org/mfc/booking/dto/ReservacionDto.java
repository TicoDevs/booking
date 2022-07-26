package org.mfc.booking.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mfc.booking.entidad.Cita;
import org.mfc.booking.entidad.DetalleReservacionProd;
import org.mfc.booking.entidad.Producto;
import org.mfc.booking.seguridad.entidad.Usuario;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ReservacionDto {

    private long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaReservacion;
    private Integer estado;
    private Integer tipo;
    private UsuarioDto usuario;
    private Set<DetalleReservacionProdDto> prod = new HashSet<>();
    private Cita cita;
    private String detalleReserva;


}

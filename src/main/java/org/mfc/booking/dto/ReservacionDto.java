package org.mfc.booking.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ReservacionDto {
    private long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaReservacion;
    private Integer estado;
    private Integer tipo;
    private UsuarioDto usuario;
    private Set<DetalleReservacionProdDto> prod = new HashSet<>();
    private String detalleReserva;
}

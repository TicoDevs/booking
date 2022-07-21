package org.mfc.booking.entidad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data @AllArgsConstructor
@NoArgsConstructor
public class Reservacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date fechaReservacion;
    private Boolean estado;
    private Integer tipo;
    private long idMiembro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservacion")
    private List<DetalleReservacionProd> prodSet;
    private long idCita;
    private String detalleReserva;
}

package org.mfc.booking.entidad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.mfc.booking.seguridad.entidad.Usuario;

import javax.persistence.*;
import java.util.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date fechaReservacion;
    private Integer estado;
    private Integer tipo;
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservUsu_id")
    private Usuario usuario;
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservacion_id")
    private Set<DetalleReservacionProd> prod = new HashSet<>();
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cita_id")
    private Cita cita;
    private String detalleReserva;

}

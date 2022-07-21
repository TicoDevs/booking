package org.mfc.booking.entidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleReservacionProd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idProducto;
    private String codigo;
    private Integer cantidad;
    @ManyToOne
    @JoinColumn(name = "reservacion_id",nullable = false, updatable = false)
    private Reservacion reservacion;
}

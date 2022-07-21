package org.mfc.booking.entidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idUsuario;
    private long idUsuarioMiembro;
    private Date fechaIngreso;
    private Date ultimaModificacion;
    private Date fechaSalida;
    private String descripcion;
    private long idProducto;
    private Boolean estado;

}

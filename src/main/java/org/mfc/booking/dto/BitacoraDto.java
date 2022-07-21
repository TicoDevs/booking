package org.mfc.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraDto {

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

package org.mfc.booking.entidad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date fechaCita;
    private Boolean estado;
    private String descripcion;

    public Cita(Date fechaCita, boolean estado, String descripcion) {
        this.fechaCita = fechaCita;
        this.estado = estado;
        this.descripcion = descripcion;
    }
}

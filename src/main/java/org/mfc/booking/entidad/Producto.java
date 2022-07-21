package org.mfc.booking.entidad;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private Boolean estado;
    @ElementCollection(targetClass=String.class)
    private Set<String> img;
    @Column(unique=true)
    private String codigo;
}

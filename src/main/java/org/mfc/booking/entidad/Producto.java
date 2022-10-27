package org.mfc.booking.entidad;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private Boolean estado;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "img_prod",
        joinColumns = {
            @JoinColumn(name = "prod_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "image_id")
        }
    )
    private Set<ImageModel> imagenes;
    @Column(unique=true)
    private String codigo;
}

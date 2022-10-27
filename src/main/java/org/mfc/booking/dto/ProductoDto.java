package org.mfc.booking.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mfc.booking.entidad.ImageModel;

import javax.validation.constraints.NotNull;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {

    @NotNull
    private long id;
    @NotNull
    private String nombre;
    @NotNull
    private String descripcion;
    @NotNull
    private int cantidad;
    @NotNull
    private Boolean estado;
    private Set<ImageModel> imagenes;
    @NotNull
    private String codigo;


    public ProductoDto(String nombre, String descripcion, int cantidad, Boolean estado, Set<ImageModel> imagenes, String codigo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.estado = estado;
        this.imagenes = imagenes;
        this.codigo = codigo;
    }

    public ProductoDto(int cantidad, String codigo) {
        this.cantidad = cantidad;
        this.codigo = codigo;
    }
}

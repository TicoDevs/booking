package org.mfc.booking.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    /*@NotNull
    @Size(min = 1, message = "Al menos debe de agregar un rol")*/
    private Set<String> img;
    @NotNull
    private String codigo;


    public ProductoDto(String nombre, String descripcion, int cantidad, Boolean estado, Set<String> img, String codigo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.estado = estado;
        this.img = img;
        this.codigo = codigo;
    }

    public ProductoDto(int cantidad, String codigo) {
        this.cantidad = cantidad;
        this.codigo = codigo;
    }
}

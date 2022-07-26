package org.mfc.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CitaDto {

    private long id;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaCita;
    @NotNull
    private Boolean estado;
    @NotNull
    private String descripcion;

    public CitaDto(Date fechaCita, boolean estado, String descripcion) {
        this.fechaCita = fechaCita;
        this.estado = estado;
        this.descripcion = descripcion;
    }

}

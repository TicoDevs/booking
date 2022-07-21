package org.mfc.booking.excepcion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    private String nombreDelRecurso;
    private String nombreDelCampo;
    private String valorDelCampo;

    public ResourceNotFoundException(String nombreDelRecurso, String nombreDelCampo, String valorDelCampo) {
        super(String.format("%s no encontrad@ con : %s : '%s'", nombreDelRecurso,nombreDelCampo,valorDelCampo));
        this.nombreDelRecurso = nombreDelRecurso;
        this.nombreDelCampo = nombreDelCampo;
        this.valorDelCampo = valorDelCampo;
    }

}

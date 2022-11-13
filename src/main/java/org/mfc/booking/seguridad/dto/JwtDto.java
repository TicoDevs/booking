package org.mfc.booking.seguridad.dto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mfc.booking.seguridad.constantes.SecurityConstant;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.mfc.booking.seguridad.constantes.SecurityConstant.*;

@Data
public class JwtDto {
    private String token;
    private String bearer = TOKEN_PREFIX;
    private String nombreUsuario;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDto(String token, String nombreUsuario, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.authorities = authorities;
    }
}

package org.mfc.booking.seguridad.jwt;
import io.jsonwebtoken.*;
import org.mfc.booking.excepcion.BlogAppException;
import org.mfc.booking.seguridad.servicio.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.mfc.booking.seguridad.constantes.SecurityConstant.*;

@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;


    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, TOKEN_PREFIX);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateToken(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(TOKEN_PREFIX, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(TOKEN_PREFIX, null).path("/api").build();
        return cookie;
    }

    public String getNombreUsuarioFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST,"Firma JWT no valida");
        }
        catch (MalformedJwtException ex) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST,"Token JWT no valida");
        }
        catch (ExpiredJwtException ex) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST,"Token JWT caducado");
        }
        catch (UnsupportedJwtException ex) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST,"Token JWT no compatible");
        }
        catch (IllegalArgumentException ex) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST,"La cadena claims JWT esta vacia");
        }
    }
    public String generateToken(String username) {
        Date fechaActual = new Date();
        Date dechaExpiracion = new Date(fechaActual.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(dechaExpiracion)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }
}

package org.mfc.booking.seguridad.jwt;

import org.mfc.booking.excepcion.BlogAppException;
import org.mfc.booking.seguridad.servicio.UsuarioDetalleServicioImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mfc.booking.seguridad.constantes.SecurityConstant.AUTHORIZATION;
import static org.mfc.booking.seguridad.constantes.SecurityConstant.TOKEN_PREFIX;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UsuarioDetalleServicioImpl usuarioDetalleServicioImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseJwt(req);
            System.out.println("req = " + req + ", res = " + res + ", filterChain = " + filterChain + "token= " + token);
            if( token != null &&  !token.equals("") &&  jwtProvider.validateToken(token)){
                String nombreUsuario = jwtProvider.getNombreUsuarioFromToken(token);
                UserDetails userDetails = usuarioDetalleServicioImpl.loadUserByUsername(nombreUsuario);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(req, res);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtProvider.getJwtFromCookies(request);
        return jwt;
    }
 /*   private String getToken(HttpServletRequest request){
        String header = request.getHeader(AUTHORIZATION);
        if(header != null && header.startsWith(TOKEN_PREFIX))
            return header.replace(TOKEN_PREFIX, "");
        return null;
    }*/
}


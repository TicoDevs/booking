package org.mfc.booking.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String nombreUsuario;
    private String email;
    private List<String> roles = new ArrayList<>();
}

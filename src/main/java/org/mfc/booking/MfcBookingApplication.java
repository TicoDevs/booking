package org.mfc.booking;

import org.mfc.booking.dto.UsuarioDto;
import org.mfc.booking.seguridad.entidad.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootApplication
public class MfcBookingApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(MfcBookingApplication.class, args);
	}

}

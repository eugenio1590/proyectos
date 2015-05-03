package com.prueba.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.prueba.web.configuracion.dao.UsuarioRepository;
import com.prueba.web.model.Usuario;
import com.prueba.web.seguridad.service.ServicioHistorial;

@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	@Autowired
	private ServicioHistorial servicioHistorial;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	//private UsuarioDAO usuarioDAO;
	
	/**METODOS OVERRIDE*/
	@Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		String clave = user.getPassword();
		List<Usuario> listUsuario = 
				usuarioRepository.findByUsernameContainingIgnoreCaseOrPaswordContainingIgnoreCase(
						user.getUsername(), (clave==null) ? "" : clave);
		Usuario usuario = listUsuario.get(0);
		servicioHistorial.cerarHistorialSession(usuario);
		setDefaultTargetUrl("/");
		super.onLogoutSuccess(request, response, authentication);
	}
}

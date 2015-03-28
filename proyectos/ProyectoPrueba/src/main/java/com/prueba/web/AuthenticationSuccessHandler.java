package com.prueba.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.prueba.web.dao.configuracion.UsuarioDAO;
import com.prueba.web.model.Usuario;
import com.prueba.web.service.seguridad.ServicioHistorial;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private ServicioHistorial servicioHistorial;
	
	@Autowired
	private UsuarioDAO usuarioDAO;

	/**METODOS OVERRIDE*/
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
    		Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		Usuario usuario = usuarioDAO.consultarUsuario(user.getUsername(), user.getPassword());
		servicioHistorial.registrarHistorialSession(usuario);
		setDefaultTargetUrl("/admin/home");
		super.onAuthenticationSuccess(request, response, authentication);
	}

}

package com.prueba.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.prueba.web.configuracion.dao.UsuarioRepository;
import com.prueba.web.model.Usuario;
import com.prueba.web.seguridad.service.ServicioHistorial;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private ServicioHistorial servicioHistorial;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	//private UsuarioDAO usuarioDAO;

	/**METODOS OVERRIDE*/
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
    		Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		String clave = user.getPassword();
		List<Usuario> listUsuario = 
				usuarioRepository.findByUsernameContainingIgnoreCaseOrPaswordContainingIgnoreCase(
						user.getUsername(), (clave==null) ? "" : clave);
		Usuario usuario = listUsuario.get(0);
		servicioHistorial.registrarHistorialSession(usuario);
		setDefaultTargetUrl("/admin/home");
		super.onAuthenticationSuccess(request, response, authentication);
	}

}

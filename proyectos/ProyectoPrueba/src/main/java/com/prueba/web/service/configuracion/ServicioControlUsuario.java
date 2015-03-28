package com.prueba.web.service.configuracion;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import com.prueba.web.model.Menu;
import com.prueba.web.model.Usuario;

public interface ServicioControlUsuario {
	//Usuarios
	public Usuario consultarUsuario(Integer id);
	public Usuario consultarUsuario(String usuario, String clave);
	public Usuario grabarUsuario(Usuario usuario);
	public Usuario actualizarUsuario(Usuario usuario, boolean encriptar);
	public Boolean cambiarEstadoUsuario(Usuario usuario, boolean estado);
	public Boolean eliminarUsuario(Usuario usuario);
	public Boolean validarAutenticacion(User user);
	public UsernamePasswordAuthenticationToken consultarAutenticacion(User user);
	public Map<String, Object> consultarUsuarios(Integer id, String username, Boolean activo,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	//Menu
	public List<Menu> consultarMenus();
}

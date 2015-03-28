package com.prueba.web.configuracion.dao;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Usuario;

public interface UsuarioDAO extends IGenericDao<Usuario, Integer> {
	public Usuario consultarUsuario(String usuario, String clave);
	public List<Usuario> consultarUsuarios(Usuario usuarioF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);
}

package com.prueba.web.dao.configuracion;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Usuario;

public interface UsuarioDAO extends IGenericDao<Usuario, Integer> {
	public Usuario consultarUsuario(String usuario, String clave);
	public List<Usuario> consultarUsuarios(String id, String username, Boolean isActivo, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);
}

package com.prueba.web.dao.seguridad.configuracion;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Group;

public interface GrupoDAO extends IGenericDao<Group, Integer> {
	public List<Group> consultarGrupos(String nombre, String fieldSort, Boolean sortDirection, int pagina, int limite);
}

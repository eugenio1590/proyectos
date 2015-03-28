package com.prueba.web.seguridad.configuracion.dao;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Group;

public interface GrupoDAO extends IGenericDao<Group, Integer> {
	public List<Group> consultarGrupos(Group grupoF, String fieldSort, Boolean sortDirection, int pagina, int limite);
}

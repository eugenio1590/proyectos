package com.prueba.web.configuracion.dao;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Arbol;

public interface ArbolDAO<T extends Arbol> extends IGenericDao<T, Integer> {
	public List<T> consultarSubRamas(int idPadre, int start, int limit);
}

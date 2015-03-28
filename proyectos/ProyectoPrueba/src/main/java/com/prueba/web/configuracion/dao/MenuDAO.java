package com.prueba.web.configuracion.dao;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Menu;

public interface MenuDAO extends IGenericDao<Menu, Integer> {
	public List<Menu> consultarPadres();
}
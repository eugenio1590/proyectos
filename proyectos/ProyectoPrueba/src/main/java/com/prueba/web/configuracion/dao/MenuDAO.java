package com.prueba.web.configuracion.dao;

import java.util.List;

import com.prueba.web.model.Menu;

public interface MenuDAO extends ArbolDAO<Menu> {
	public List<Menu> consultarRootPadres(int start, int limit);
	public List<Menu> consultarHijosNoAsignadoGrupo(int idGrupo, int idPadre, int start, int limit);
	
	//Se Eliminaran
	public List<Menu> consultarPadres();
}

package com.prueba.web.seguridad.configuracion.dao;

import java.util.List;

import com.prueba.web.configuracion.dao.ArbolDAO;
import com.prueba.web.model.GroupMenu;

public interface GroupMenuDAO extends ArbolDAO<GroupMenu> {
	public List<GroupMenu> consultarPadresMenuAsignadoGrupo(int idGrupo, int start, int limit);
	public List<GroupMenu> consultarNodosDistintosHijosMenuUsuario(int idUsuario, int start, int limit);
}

package com.prueba.web.seguridad.configuracion.service;

import java.util.Map;

import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMenu;
import com.prueba.web.model.Persona;

public interface ServicioControlGrupo {
	//Grupos
	public Map<String, Object> consultarGrupos(Group grupoF, String fieldSort, Boolean sortDirection, int pagina, int limit);
	public Group consultarGrupoId(Integer id);
	public Group registrarOActualizarGrupo(Group grupo);
	public Boolean eliminarGrupo(Group grupo);
	//Miembros de Grupos
	public Map<String, Object> consultarMiembrosGrupo(Persona personaF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	//Menu de Grupos
	public Map<String, Object> consultarPadresMenuAsignadoGrupo(int idGrupo, int pagina, int limit);
	public GroupMenu actualizarGroupMenu(GroupMenu groupMenu);
}

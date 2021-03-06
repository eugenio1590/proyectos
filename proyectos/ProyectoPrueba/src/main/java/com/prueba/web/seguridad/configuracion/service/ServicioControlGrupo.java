package com.prueba.web.seguridad.configuracion.service;

import java.util.List;
import java.util.Map;

import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMember;
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
	public void actualizarMiembrosGrupo(Group grupo, List<GroupMember> miembrosAgregar, List<GroupMember> miembrosEliminar);
	//Menu de Grupos
	public Map<String, Object> consultarPadresMenuAsignadoGrupo(int idGrupo, int pagina, int limit);
	public boolean actualizarGroupMenu(List<GroupMenu> menuNuevo, int idGrupo, int pagina, int limit);
	public Map<String, Object> consultarNodosDistintosHijosMenuUsuario(int idUsuario);
}

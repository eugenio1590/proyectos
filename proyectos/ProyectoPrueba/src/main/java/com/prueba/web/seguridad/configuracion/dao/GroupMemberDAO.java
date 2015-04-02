package com.prueba.web.seguridad.configuracion.dao;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.Persona;

public interface GroupMemberDAO extends IGenericDao<GroupMember, Integer> {
	public List<GroupMember> consultarUsuariosAsignadosGrupo(Persona personaF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
}

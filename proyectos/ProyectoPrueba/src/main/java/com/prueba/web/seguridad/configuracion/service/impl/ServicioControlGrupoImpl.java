package com.prueba.web.seguridad.configuracion.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.web.seguridad.configuracion.dao.GroupMemberDAO;
import com.prueba.web.seguridad.configuracion.dao.GroupMenuDAO;
import com.prueba.web.seguridad.configuracion.dao.GrupoDAO;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMenu;
import com.prueba.web.model.Persona;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.service.impl.AbstractServiceImpl;
import com.prueba.web.seguridad.configuracion.service.ServicioControlGrupo;

@Service
public class ServicioControlGrupoImpl extends AbstractServiceImpl implements ServicioControlGrupo {

	@Autowired
	@BeanInjector("grupoDAO")
	private GrupoDAO grupoDAO;
	
	@Autowired
	@BeanInjector("groupMemberDAO")
	private GroupMemberDAO groupMemberDAO;
	
	@Autowired
	@BeanInjector("groupMenuDAO")
	private GroupMenuDAO groupMenuDAO;
	
	//Grupos
	@Override
	public Map<String, Object> consultarGrupos(Group grupoF, String fieldSort, Boolean sortDirection, int pagina, int limit) {
		// TODO Auto-generated method stub
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", grupoDAO.consultarGrupos(grupoF, fieldSort, sortDirection, 0, -1).size());
		parametros.put("grupos", grupoDAO.consultarGrupos(grupoF, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	@Override
	public Group consultarGrupoId(Integer id) {
		// TODO Auto-generated method stub
		return grupoDAO.findByPrimaryKey(id);
	}
	
	@Override
	public Group registrarOActualizarGrupo(Group grupo) {
		// TODO Auto-generated method stub
		grupo.setAuthority("ROLE_"+grupo.getGroupName().replaceAll(" ", "_").toUpperCase());
		if(grupo.getId()!=null)
			return grupoDAO.update(grupo);
		else
			return grupoDAO.save(grupo);
	}
	
	@Override
	public Boolean eliminarGrupo(Group grupo) {
		// TODO Auto-generated method stub
		if(consultarGrupoId(grupo.getId())!=null)
			grupoDAO.delete(grupo);
		else
			return false;
		
		return true;
	}

	//Miembros de Grupo
	@Override
	public Map<String, Object> consultarMiembrosGrupo(Persona personaF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", groupMemberDAO.consultarUsuariosAsignadosGrupo(personaF, idGrupo, fieldSort, sortDirection, 0, -1).size());
		parametros.put("miembros", groupMemberDAO.consultarUsuariosAsignadosGrupo(personaF, idGrupo, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	//Menu de Grupos
	@Override
	public Map<String, Object> consultarPadresMenuAsignadoGrupo(int idGrupo, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", groupMenuDAO.consultarPadresMenuAsignadoGrupo(idGrupo, 0, -1).size());
		parametros.put("menu", groupMenuDAO.consultarPadresMenuAsignadoGrupo(idGrupo, pagina*limit, limit));
		return parametros;
	}
	
	@Override
	public GroupMenu actualizarGroupMenu(GroupMenu groupMenu) {
		// TODO Auto-generated method stub
		if(groupMenu.getId()!=null)
			return this.groupMenuDAO.save(groupMenu);
		else
			return this.groupMenuDAO.update(groupMenu);
	}
	
	/**SETTERS Y GETTERS*/
	public GrupoDAO getGrupoDAO() {
		return grupoDAO;
	}

	public void setGrupoDAO(GrupoDAO grupoDAO) {
		this.grupoDAO = grupoDAO;
	}

	public GroupMemberDAO getGroupMemberDAO() {
		return groupMemberDAO;
	}

	public void setGroupMemberDAO(GroupMemberDAO groupMemberDAO) {
		this.groupMemberDAO = groupMemberDAO;
	}

	public GroupMenuDAO getGroupMenuDAO() {
		return groupMenuDAO;
	}

	public void setGroupMenuDAO(GroupMenuDAO groupMenuDAO) {
		this.groupMenuDAO = groupMenuDAO;
	}
}

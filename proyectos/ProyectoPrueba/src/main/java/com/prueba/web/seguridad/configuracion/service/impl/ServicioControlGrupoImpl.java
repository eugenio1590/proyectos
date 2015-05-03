package com.prueba.web.seguridad.configuracion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.prueba.web.seguridad.configuracion.dao.GroupMemberRepository;
import com.prueba.web.seguridad.configuracion.dao.GroupMenuRepository;
import com.prueba.web.seguridad.configuracion.dao.GrupoRepository;
import com.prueba.web.seguridad.configuracion.dao.impl.GroupMemberDAO;
import com.prueba.web.seguridad.configuracion.dao.impl.GroupMenuDAO;
import com.prueba.web.seguridad.configuracion.dao.impl.GrupoDAO;
import com.prueba.web.configuracion.dao.impl.MenuDAO;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.GroupMenu;
import com.prueba.web.model.Menu;
import com.prueba.web.model.Persona;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.service.impl.AbstractServiceImpl;
import com.prueba.web.seguridad.configuracion.service.ServicioControlGrupo;

@Service
public class ServicioControlGrupoImpl extends AbstractServiceImpl implements ServicioControlGrupo {

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	@Autowired
	private GroupMenuRepository groupMenuRepository;
	
	//Grupos
	@Override
	public Map<String, Object> consultarGrupos(Group grupoF, String fieldSort, Boolean sortDirection, int pagina, int limit) {
		// TODO Auto-generated method stub
		GrupoDAO grupoDAO = new GrupoDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Group> pageGroup = null;
		List<Group> grupos = new ArrayList<Group>();
		
		Direction direction = Sort.Direction.ASC;
		String field = "id";
		
		if(fieldSort!=null && sortDirection!=null){
			direction = (sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
			field = fieldSort;
		}
		if(grupoF!=null) {
			pageGroup = grupoRepository.findAll(grupoDAO.findByExample(grupoF), 
					new PageRequest(pagina, limit, direction , field));
			grupos = pageGroup.getContent();
		}
		
		parametros.put("total", Long.valueOf((pageGroup!=null) ? pageGroup.getTotalElements():0).intValue());
		parametros.put("grupos", grupos);
		return parametros;
	}
	
	@Override
	public Group consultarGrupoId(Integer id) {
		// TODO Auto-generated method stub
		return grupoRepository.getOne(id);
	}
	
	@Override
	public Group registrarOActualizarGrupo(Group grupo) {
		// TODO Auto-generated method stub
		grupo.setAuthority("ROLE_"+grupo.getGroupName().replaceAll(" ", "_").toUpperCase());
		return grupoRepository.save(grupo);
	}
	
	@Override
	public Boolean eliminarGrupo(Group grupo) {
		// TODO Auto-generated method stub
		if(consultarGrupoId(grupo.getId())!=null)
			grupoRepository.delete(grupo);
		else
			return false;
		
		return true;
	}

	//Miembros de Grupo
	@Override
	public Map<String, Object> consultarMiembrosGrupo(Persona personaF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit){
		GroupMemberDAO groupMemberDAO = new GroupMemberDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<GroupMember> pageGroupMember = groupMemberRepository.findAll(
				groupMemberDAO.consultarUsuariosAsignadosGrupo(personaF, idGrupo, fieldSort, sortDirection), 
				new PageRequest(pagina, limit));
		parametros.put("total", pageGroupMember.getTotalElements());
		parametros.put("miembros", pageGroupMember.getContent());
		return parametros;
	}
	
	//Menu de Grupos
	@Override
	public Map<String, Object> consultarPadresMenuAsignadoGrupo(int idGrupo, int pagina, int limit){
		GroupMenuDAO groupMenuDAO = new GroupMenuDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<GroupMenu> pageGroupMenu = groupMenuRepository.findAll(
				groupMenuDAO.consultarPadresMenuAsignadoGrupo(idGrupo), new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageGroupMenu.getTotalElements()).intValue());
		parametros.put("menu", pageGroupMenu.getContent());
		return parametros;
	}
	
	@Override
	public boolean actualizarGroupMenu(List<GroupMenu> menuNuevo, int idGrupo, int pagina, int limit) {
		// TODO Auto-generated method stub
		boolean exito = true;
		try {
			GroupMenuDAO groupMenuDAO = new GroupMenuDAO();
			Page<GroupMenu> pageGroupMenu = groupMenuRepository.findAll(
					groupMenuDAO.consultarPadresMenuAsignadoGrupo(idGrupo), new PageRequest(pagina, limit));
			List<GroupMenu> padres = pageGroupMenu.getContent();
			for(GroupMenu padre : padres)
				groupMenuRepository.delete(padre);

			for(GroupMenu nodo : menuNuevo)
				if(nodo.getPadre()==null) //La relacion Cascade del Padre registra a sus hijos tambien.
					groupMenuRepository.save(nodo);
		}
		catch (Exception e){
			System.out.println("Error al Actualizar el Menu del Grupo: "+e.getMessage());
			exito = false;
		}
		
		return exito;
	}
	
	@Override
	public Map<String, Object> consultarNodosDistintosHijosMenuUsuario(int idUsuario) {
		// TODO Auto-generated method stub
		GroupMenuDAO groupMenuDAO = new GroupMenuDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		List<GroupMenu> listGroupMenu = groupMenuRepository.findAll(
				groupMenuDAO.consultarNodosDistintosHijosMenuUsuario(idUsuario));
		parametros.put("total", listGroupMenu.size());
		parametros.put("menu", listGroupMenu);
		return parametros;
	}
}

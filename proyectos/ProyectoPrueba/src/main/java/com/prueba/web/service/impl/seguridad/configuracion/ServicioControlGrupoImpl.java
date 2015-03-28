package com.prueba.web.service.impl.seguridad.configuracion;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.web.dao.seguridad.configuracion.GrupoDAO;
import com.prueba.web.model.Group;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.service.impl.AbstractServiceImpl;
import com.prueba.web.service.seguridad.configuracion.ServicioControlGrupo;

@Service
public class ServicioControlGrupoImpl extends AbstractServiceImpl implements ServicioControlGrupo {

	@Autowired
	@BeanInjector("grupoDAO")
	private GrupoDAO grupoDAO;
	
	@Override
	public Map<String, Object> consultarGrupos(String nombre, String fieldSort, Boolean sortDirection, int pagina, int limit) {
		// TODO Auto-generated method stub
		nombre = (nombre!=null) ? "%"+nombre+"%" : null;
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", ((Integer) grupoDAO.consultarGrupos(nombre, fieldSort, sortDirection, 0, -1).size()).longValue());
		parametros.put("grupos", grupoDAO.consultarGrupos(nombre, fieldSort, sortDirection, pagina*limit, limit));
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

	/**SETTERS Y GETTERS*/
	public GrupoDAO getGrupoDAO() {
		return grupoDAO;
	}

	public void setGrupoDAO(GrupoDAO grupoDAO) {
		this.grupoDAO = grupoDAO;
	}

	
}

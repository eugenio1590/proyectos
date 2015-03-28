package com.prueba.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.prueba.web.dao.ArticuloDAO;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.service.ServiceControlInventario;

@Service
//@Secured(value = {"ROLE_ADMINS"})
public class ServiceControlInventarioImpl extends AbstractServiceImpl implements ServiceControlInventario {
	
	@Autowired
	@BeanInjector("articuloDAO")
	private ArticuloDAO articuloDAO;
	
	@Override
	public Map<String, Object> listarArticulos(int pagina, int limit) {
		// TODO Auto-generated method stub
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", articuloDAO.countAll());
		parametros.put("articulos", articuloDAO.findAll(pagina*limit, limit));
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarArticulosCodigoONombre(String codigo,
			String nombre, int pagina, int limit) {
		// TODO Auto-generated method stub
		codigo = (codigo!=null) ? "%"+codigo+"%" : null;
		nombre = (nombre!=null) ? "%"+nombre+"%" : null;
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", ((Integer) articuloDAO.consultarArticulosCodigoONombre(codigo, nombre, 0, -1).size()).longValue());
		parametros.put("articulos", articuloDAO.consultarArticulosCodigoONombre(codigo, nombre, pagina*limit, limit));
		return parametros;
	}	

	/**SETTERS Y GETTERS*/
	public ArticuloDAO getArticuloDAO() {
		return articuloDAO;
	}

	public void setArticuloDAO(ArticuloDAO articuloDAO) {
		this.articuloDAO = articuloDAO;
	}
}

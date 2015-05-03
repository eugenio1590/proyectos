package com.prueba.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.prueba.web.inventario.dao.ArticuloRepository;
import com.prueba.web.inventario.dao.impl.ArticuloDAO;
import com.prueba.web.model.Articulo;
import com.prueba.web.service.ServiceControlInventario;

@Service
//@Secured(value = {"ROLE_ADMINS"})
public class ServiceControlInventarioImpl extends AbstractServiceImpl implements ServiceControlInventario {
	
	@Autowired
	private ArticuloRepository articuloRepository;
	
	@Override
	public Map<String, Object> listarArticulos(int pagina, int limit) {
		// TODO Auto-generated method stub
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Articulo> pageArticulo = articuloRepository.findAll(new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageArticulo.getTotalElements()).intValue());
		parametros.put("articulos", pageArticulo.getContent());
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarArticulosCodigoONombre(String codigo,
			String nombre, int pagina, int limit) {
		// TODO Auto-generated method stub
		ArticuloDAO articuloDAO = new ArticuloDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Articulo> pageArticulo = articuloRepository.findAll(
				articuloDAO.findByExample(new Articulo(codigo, nombre)), 
				new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageArticulo.getTotalElements()).intValue());
		parametros.put("articulos", pageArticulo.getContent());
		return parametros;
	}
}

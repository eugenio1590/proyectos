package com.prueba.web.dao;

import java.util.List;

import com.prueba.web.model.Articulo;

public interface ArticuloDAO extends IGenericDao<Articulo, Integer> {
	public List<Articulo> consultarArticulosCodigoONombre(String codigo, String nombre, int start, int limit);
}

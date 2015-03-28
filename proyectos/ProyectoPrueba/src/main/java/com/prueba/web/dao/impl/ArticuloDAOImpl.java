package com.prueba.web.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.ArticuloDAO;
import com.prueba.web.model.Articulo;

@Repository
public class ArticuloDAOImpl extends AbstractJpaDao<Articulo, Integer> implements ArticuloDAO {

	public ArticuloDAOImpl() {
		// TODO Auto-generated constructor stub
		super(Articulo.class);
	}

	@Override
	public List<Articulo> consultarArticulosCodigoONombre(String codigo,
			String nombre, int start, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		this.crearJoins(null);

		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		if(codigo!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("codigo")), codigo.toLowerCase()
			));
		
		if(nombre!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("nombre")), nombre.toLowerCase()
			));

		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("codigo", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}

	
}

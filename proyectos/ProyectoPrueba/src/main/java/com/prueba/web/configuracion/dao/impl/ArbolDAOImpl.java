package com.prueba.web.configuracion.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.prueba.web.configuracion.dao.ArbolDAO;
import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.Arbol;
import com.prueba.web.model.Menu;

public class ArbolDAOImpl<T extends Arbol> extends AbstractJpaDao<T, Integer> implements ArbolDAO<T> {

	public ArbolDAOImpl() {
		super(Arbol.class);
		// TODO Auto-generated constructor stub
	}
	
	public ArbolDAOImpl(Class<? extends Arbol> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<T> consultarSubRamas(int idPadre, int start, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		Map<String, JoinType> entidades=new HashMap<String, JoinType>();
		entidades.put("padre", JoinType.INNER);
		Map<String, Join> joins = this.crearJoins(entidades);
			
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
			
		restricciones.add(this.criteriaBuilder.equal(joins.get("padre").get("id"), idPadre));
		
		restricciones.add(this.criteriaBuilder.isNotEmpty(this.entity.<List>get("hijos")));
			
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}

}

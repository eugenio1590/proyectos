package com.prueba.web.configuracion.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.Arbol;

public class ArbolDAO<T extends Arbol> extends AbstractJpaDao<T>{
	
	public Specification<T> consultarSubRamas(final int idPadre) {
		// TODO Auto-generated method stub
		return new Specification<T>() {

			@Override
			public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("padre", JoinType.INNER);
				Map<String, Join> joins = crearJoins(entidades);
				
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				restricciones.add(criteriaBuilder.equal(joins.get("padre").get("id"), idPadre));
				
				restricciones.add(criteriaBuilder.isNotEmpty(entity.<List>get("hijos")));
				
				//3. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				orders.add(criteriaBuilder.asc(entity.get("id")));
				
				return crearPredicate(restricciones);
			}
		};
	}

}

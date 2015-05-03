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

import com.prueba.web.model.Menu;

public class MenuDAO extends ArbolDAO<Menu> {
	
	public Specification<Menu> consultarHijosNoAsignadoGrupo(final int idGrupo, final int idPadre) {
		// TODO Auto-generated method stub
		return new Specification<Menu>() {

			@Override
			public Predicate toPredicate(Root<Menu> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("padre", JoinType.INNER);
				entidades.put("groupMenus", JoinType.LEFT);
				Map<String, Join> joins = crearJoins(entidades);
				
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(criteriaBuilder.equal(joins.get("padre").get("id"), idPadre));
				restricciones.add(criteriaBuilder.isEmpty(entity.<List>get("hijos")));
				Join joinGroupMenu = joins.get("groupMenus");
				Join joinGroup = joinGroupMenu.join("group", JoinType.LEFT);
				restricciones.add(criteriaBuilder.or(
						criteriaBuilder.isNull(joinGroupMenu.get("id")),
						/**Condicion Simplificada*/
						criteriaBuilder.notEqual(joinGroup.get("id"), idGrupo),
						criteriaBuilder.notEqual(
								joinGroupMenu.join("menu", JoinType.LEFT).get("id"), 
								entity.get("id")
						)
				));
				
				/**Condicion Creada*/
				/*this.criteriaBuilder.not(
					this.criteriaBuilder.and(
						this.criteriaBuilder.equal(joinGroup.get("id"), idGrupo),
						this.criteriaBuilder.equal(
								joinGroupMenu.join("menu", JoinType.LEFT).get("id"), 
								this.entity.get("id")
						)
					)
				)*/
				
				//3. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				orders.add(criteriaBuilder.asc(entity.get("id")));
				
				return crearPredicate(restricciones);
			}
			
		};
	}
}

package com.prueba.web.seguridad.configuracion.dao.impl;

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
import javax.persistence.criteria.Selection;

import org.springframework.data.jpa.domain.Specification;

import com.prueba.web.configuracion.dao.impl.ArbolDAO;
import com.prueba.web.model.GroupMenu;

public class GroupMenuDAO extends ArbolDAO<GroupMenu> {
	
	public Specification<GroupMenu> consultarPadresMenuAsignadoGrupo(final int idGrupo) {
		// TODO Auto-generated method stub
		return new Specification<GroupMenu>() {

			@Override
			public Predicate toPredicate(Root<GroupMenu> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("group", JoinType.INNER);
				Map<String, Join> joins = crearJoins(entidades);
				
				List<Predicate> restricciones = new ArrayList<Predicate>();

				restricciones.add(
						criteriaBuilder.isNull(entity.get("padre"))
				);
				
				restricciones.add(
						criteriaBuilder.equal(joins.get("group").get("id"), idGrupo)
				);

				//3. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				orders.add(criteriaBuilder.asc(entity.get("id")));
				
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	public Specification<GroupMenu> consultarNodosDistintosHijosMenuUsuario(final int idUsuario) {
		// TODO Auto-generated method stub
		return new Specification<GroupMenu>() {

			@Override
			public Predicate toPredicate(Root<GroupMenu> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades=new HashMap<String, JoinType>();
				entidades.put("group", JoinType.INNER);
				Map<String, Join>joins=crearJoins(entidades);
				
				criteriaQuery.distinct(true);
				criteriaQuery.multiselect(new Selection[]{
						entity.get("menu")
				});
				
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(
						criteriaBuilder.isEmpty(entity.<List>get("hijos"))
						);
				restricciones.add(
						criteriaBuilder.equal(joins.get("group").join("groupMembers").join("usuario").get("id"), idUsuario)
						);
				
				//3. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				orders.add(criteriaBuilder.asc(entity.get("menu")));
				
				return crearPredicate(restricciones);
			}
		};
	}	
}
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

import org.springframework.data.jpa.domain.Specification;

import com.prueba.web.dao.AbstractJpaDao;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.Persona;

public class GroupMemberDAO extends AbstractJpaDao<GroupMember> {
	
	public Specification<GroupMember> consultarUsuariosAsignadosGrupo(final Persona personaF, 
			final int idGrupo, final String fieldSort, final Boolean sortDirection) {
		// TODO Auto-generated method stub
		return new Specification<GroupMember>() {

			@Override
			public Predicate toPredicate(Root<GroupMember> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("usuario", JoinType.INNER);
				entidades.put("group", JoinType.INNER);
				Map<String, Join> joins = crearJoins(entidades);
				
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				agregarFiltros(personaF, restricciones, joins);
				
				restricciones.add(criteriaBuilder.equal(
						joins.get("group").get("id"),
						idGrupo
				));
				
				List<Order> orders = new ArrayList<Order>();
				
				if(fieldSort!=null && sortDirection!=null)
					agregarOrder(fieldSort, sortDirection, orders, joins);
				else
					orders.add(criteriaBuilder.asc(entity.get("id")));
				
				//3. Creamos los campos de ordenamiento y ejecutamos
				return crearPredicate(restricciones);
			}
		};
	}

	/**METODOS PROPIOS DE LA CLASE*/
	private void agregarFiltros(Persona persona, List<Predicate> restricciones, Map<String, Join> joins){
		if(persona!=null){
			if(persona.getCedula()!=null)
				restricciones.add(agregarFiltro("cedula", joins, persona.getCedula()));

			if(persona.getNombre()!=null)
				restricciones.add(agregarFiltro("nombre", joins, persona.getNombre()));

			if(persona.getApellido()!=null)
				restricciones.add(agregarFiltro("apellido", joins, persona.getApellido()));
		}
	}
	
	private Predicate agregarFiltro(String field, Map<String, Join> joins, String value){
		return this.criteriaBuilder.like(
				this.criteriaBuilder.lower(
						joins.get("usuario").join("persona").get(field)
				),
				"%"+value.toLowerCase()+"%"
			);
	}
	
	private void agregarOrder(String fieldSort, Boolean sortDirection, List<Order> orders, Map<String, Join> joins){
		Order order = (sortDirection) 
				? criteriaBuilder.asc(joins.get("usuario").join("persona").get(fieldSort)) 
					: criteriaBuilder.desc(joins.get("usuario").join("persona").get(fieldSort));
		orders.add(order);
	}

}

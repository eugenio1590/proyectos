package com.prueba.web.seguridad.configuracion.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.Persona;
import com.prueba.web.seguridad.configuracion.dao.GroupMemberDAO;

@Repository
public class GroupMemberDAOImpl extends AbstractJpaDao<GroupMember, Integer> implements GroupMemberDAO {

	public GroupMemberDAOImpl() {
		super(GroupMember.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<GroupMember> consultarUsuariosAsignadosGrupo(Persona personaF, int idGrupo, 
			String fieldSort, Boolean sortDirection, int pagina, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();
		
		//2. Generamos los Joins
		Map<String, JoinType> entidades = new HashMap<String, JoinType>();
		entidades.put("usuario", JoinType.INNER);
		entidades.put("group", JoinType.INNER);
		Map<String, Join> joins = this.crearJoins(entidades);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		
		agregarFiltros(personaF, restricciones, joins);
		
		restricciones.add(this.criteriaBuilder.equal(
				joins.get("group").get("id"),
				idGrupo
		));
		
		List<Order> orders = new ArrayList<Order>();
		
		if(fieldSort!=null && sortDirection!=null)
			agregarOrder(fieldSort, sortDirection, orders, joins);
		else
			orders.add(this.criteriaBuilder.asc(this.entity.get("id")));
		
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, pagina, limit);
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

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
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.Persona;
import com.prueba.web.model.Usuario;

public class UsuarioDAO extends AbstractJpaDao {
	
	public Specification<Usuario> consultarUsuarios(final Usuario usuarioF, 
			final String fieldSort, final Boolean sortDirection) {
		// TODO Auto-generated method stub
		return new Specification<Usuario>(){

			@Override
			public Predicate toPredicate(Root<Usuario> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarFiltros(usuarioF, restricciones);
				
				//3. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				
				if(fieldSort!=null && sortDirection!=null)
					agregarOrder(fieldSort, sortDirection, orders);
				else
					agregarOrder("id", true, orders);
				
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	public Specification<Usuario> consultarUsuariosAsignadosGrupo(final Persona usuarioF, final int idGrupo,
			final String fieldSort, final Boolean sortDirection){
		// TODO Auto-generated method stub
		return new Specification<Usuario>(){

			@Override
			public Predicate toPredicate(Root<Usuario> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("groupMembers", JoinType.INNER);
				entidades.put("persona", JoinType.INNER);
				Map<String, Join> joins = crearJoins(entidades);
				
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarFiltros(usuarioF, restricciones, joins);
				restricciones.add(criteriaBuilder.equal(
						joins.get("groupMembers").join("group").get("id"),
						idGrupo
				));
				
				//3. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				
				if(fieldSort!=null && sortDirection!=null)
					agregarOrder(fieldSort, sortDirection, orders, joins);
				else
					orders.add(criteriaBuilder.asc(entity.get("id")));
				
				return crearPredicate(restricciones);
			}
								
		};
	}
	
	public Specification<Usuario> consultarUsuariosNoAsignadosGrupo(final Persona usuarioF, 
			final int idGrupo, final String fieldSort, final Boolean sortDirection) {
		// TODO Auto-generated method stub
		return new Specification<Usuario>() {

			@Override
			public Predicate toPredicate(Root<Usuario> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub				
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("persona", JoinType.INNER);
				Map<String, Join> joins = crearJoins(entidades);
				
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarFiltros(usuarioF, restricciones, joins);
				
					//2.1 Creacion de un subquery
				Subquery<GroupMember> subquery = criteriaQuery.subquery(GroupMember.class);
				Root<GroupMember> fromGroupMember = subquery.from(GroupMember.class);
				subquery.select(fromGroupMember);
				subquery.where(
						criteriaBuilder.and(
								criteriaBuilder.equal(fromGroupMember.join("group").get("id"), idGrupo),
								criteriaBuilder.equal(fromGroupMember.get("usuario"), entity.get("username"))
						)
				);
					//2.2 Restricciones Principales
				restricciones.add(
						criteriaBuilder.or(
								criteriaBuilder.isEmpty(entity.<List>get("groupMembers")),
								criteriaBuilder.not(criteriaBuilder.exists(subquery))
						)
				);
				
				//3. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				
				if(fieldSort!=null && sortDirection!=null)
					agregarOrder(fieldSort, sortDirection, orders, joins);
				else
					orders.add(criteriaBuilder.asc(entity.get("id")));
				
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	private void agregarFiltros(Usuario usuario, List<Predicate> restricciones){
		if(usuario!=null){
			if(usuario.getId()!=null)
				restricciones.add(criteriaBuilder.like(
						entity.get("id").as(String.class), 
						"%"+String.valueOf(usuario.getId()).toLowerCase()+"%"
				));

			if(usuario.getUsername()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(entity.get("username").as(String.class)),
						"%"+usuario.getUsername().toLowerCase()+"%"
				));

			if(usuario.getActivo()!=null)
				restricciones.add(criteriaBuilder.equal(
						entity.get("activo"), 
						usuario.getActivo()
				));
		}
	}
	
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
		return criteriaBuilder.like(
				criteriaBuilder.lower(joins.get("persona").get(field)),
				"%"+value.toLowerCase()+"%"
			);
	}
	
	private void agregarOrder(String fieldSort, Boolean sortDirection, List<Order> orders, Map<String, Join> joins){
		Order order = (sortDirection) 
				? criteriaBuilder.asc(joins.get("persona").get(fieldSort)) 
					: criteriaBuilder.desc(joins.get("persona").get(fieldSort));
		orders.add(order);
		criteriaQuery.groupBy(joins.get("persona").get(fieldSort));
	}

}

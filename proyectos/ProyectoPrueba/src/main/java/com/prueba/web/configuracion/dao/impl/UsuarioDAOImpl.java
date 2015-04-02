package com.prueba.web.configuracion.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.configuracion.dao.UsuarioDAO;
import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.Persona;
import com.prueba.web.model.Usuario;

@Repository
public class UsuarioDAOImpl extends AbstractJpaDao<Usuario, Integer> implements UsuarioDAO {
	
	public UsuarioDAOImpl() {
		super(Usuario.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Usuario consultarUsuario(String usuario, String clave) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		this.crearJoins(null);

		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		
		if(usuario!=null)
			restricciones.add(this.criteriaBuilder.equal(
					this.criteriaBuilder.lower(this.entity.<String>get("username")), usuario.toLowerCase()
			));
		
		if(clave!=null)
			restricciones.add(this.criteriaBuilder.equal(
					this.criteriaBuilder.lower(this.entity.<String>get("pasword")), clave.toLowerCase()
			));

		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		
		List<Usuario> usuarios = this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, 0, -1);
		if(usuarios.size()>0)
			return usuarios.get(0);
		else
			return null;
	}

	@Override
	public List<Usuario> consultarUsuarios(Usuario usuarioF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		this.crearJoins(null);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		
		agregarFiltros(usuarioF, restricciones);
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		
		if(fieldSort!=null && sortDirection!=null)
			orders.put(fieldSort, sortDirection);
		else
			orders.put("id", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, pagina, limit);
	}
	
	@Override
	public List<Usuario> consultarUsuariosAsignadosGrupo(Persona usuarioF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit){
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		Map<String, JoinType> entidades = new HashMap<String, JoinType>();
		entidades.put("groupMembers", JoinType.INNER);
		entidades.put("persona", JoinType.INNER);
		Map<String, Join> joins = this.crearJoins(entidades);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		
		agregarFiltros(usuarioF, restricciones, joins);
		
		restricciones.add(this.criteriaBuilder.equal(
				joins.get("groupMembers").join("group").get("id"),
				idGrupo
		));
		
		List<Order> orders = new ArrayList<Order>();
		
		if(fieldSort!=null && sortDirection!=null)
			agregarOrder(fieldSort, sortDirection, orders, joins);
		else
			orders.add(this.criteriaBuilder.asc(this.entity.get("id")));
		
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, pagina, limit);
	}
	
	@Override
	public List<Usuario> consultarUsuariosNoAsignadosGrupo(Persona usuarioF, int idGrupo, 
			String fieldSort, Boolean sortDirection, int pagina, int limit) {
		// TODO Auto-generated method stub
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(-1);
		List<Usuario> usuariosGrupo = consultarUsuariosAsignadosGrupo(null, idGrupo, null, null, 0, -1);
		for(Usuario usuario : usuariosGrupo)
			ids.add(usuario.getId());
		
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();
		
		//2. Generamos los Joins
		Map<String, JoinType> entidades = new HashMap<String, JoinType>();
		entidades.put("persona", JoinType.INNER);
		Map<String, Join> joins = this.crearJoins(entidades);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		
		agregarFiltros(usuarioF, restricciones, joins);
		
		restricciones.add(this.criteriaBuilder.not(this.entity.get("id").in(ids)));
		
		List<Order> orders = new ArrayList<Order>();
		
		if(fieldSort!=null && sortDirection!=null)
			agregarOrder(fieldSort, sortDirection, orders, joins);
		else
			orders.add(this.criteriaBuilder.asc(this.entity.get("id")));
		
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, pagina, limit);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	private void agregarFiltros(Usuario usuario, List<Predicate> restricciones){
		if(usuario!=null){
			if(usuario.getId()!=null)
				restricciones.add(this.criteriaBuilder.like(
						this.entity.<Long>get("id").as(String.class), 
						"%"+String.valueOf(usuario.getId()).toLowerCase()+"%"
				));

			if(usuario.getUsername()!=null)
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.<String>get("username")),
						"%"+usuario.getUsername().toLowerCase()+"%"
				));

			if(usuario.getActivo()!=null)
				restricciones.add(this.criteriaBuilder.equal(
						this.entity.get("activo"), 
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
		return this.criteriaBuilder.like(
				this.criteriaBuilder.lower(
						joins.get("persona").get(field)
				),
				"%"+value.toLowerCase()+"%"
			);
	}
	
	private void agregarOrder(String fieldSort, Boolean sortDirection, List<Order> orders, Map<String, Join> joins){
		Order order = (sortDirection) 
				? criteriaBuilder.asc(joins.get("persona").get(fieldSort)) 
					: criteriaBuilder.desc(joins.get("persona").get(fieldSort));
		orders.add(order);
	}

}

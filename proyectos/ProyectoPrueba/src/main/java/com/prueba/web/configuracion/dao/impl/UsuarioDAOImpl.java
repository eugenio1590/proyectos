package com.prueba.web.configuracion.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.configuracion.dao.UsuarioDAO;
import com.prueba.web.dao.impl.AbstractJpaDao;
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
		
		if(usuarioF.getId()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.entity.<Long>get("id").as(String.class), "%"+String.valueOf(usuarioF.getId()).toLowerCase()+"%"
			));
		
		if(usuarioF.getUsername()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("username")),
					"%"+usuarioF.getUsername().toLowerCase()+"%"
			));
		
		if(usuarioF.getActivo()!=null)
			restricciones.add(this.criteriaBuilder.equal(
					this.entity.get("activo"), 
					usuarioF.getActivo()
			));
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		
		if(fieldSort!=null && sortDirection!=null)
			orders.put(fieldSort, sortDirection);
		else
			orders.put("id", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, pagina, limit);
	}

}

package com.prueba.web.configuracion.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.configuracion.dao.MenuDAO;
import com.prueba.web.model.Menu;

@Repository
public class MenuDAOImpl extends ArbolDAOImpl<Menu> implements MenuDAO {

	public MenuDAOImpl() {
		super(Menu.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Menu> consultarRootPadres(int start, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		this.crearJoins(null);

		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();

		restricciones.add(
				this.criteriaBuilder.isNull(this.entity.get("padre"))
		);
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}

	@Override
	public List<Menu> consultarHijosNoAsignadoGrupo(int idGrupo, int idPadre, int start, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		Map<String, JoinType> entidades=new HashMap<String, JoinType>();
		entidades.put("padre", JoinType.INNER);
		entidades.put("groupMenus", JoinType.LEFT);
		Map<String, Join> joins = this.crearJoins(entidades);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
					
		restricciones.add(this.criteriaBuilder.equal(joins.get("padre").get("id"), idPadre));
		restricciones.add(this.criteriaBuilder.isEmpty(this.entity.<List>get("hijos")));
		Join joinGroupMenu = joins.get("groupMenus");
		Join joinGroup = joinGroupMenu.join("group", JoinType.LEFT);
		restricciones.add(this.criteriaBuilder.or(
				this.criteriaBuilder.isNull(joinGroupMenu.get("id")),
				/**Condicion Simplificada*/
				this.criteriaBuilder.notEqual(joinGroup.get("id"), idGrupo),
				this.criteriaBuilder.notEqual(
						joinGroupMenu.join("menu", JoinType.LEFT).get("id"), 
						this.entity.get("id")
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
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}
	
	@Override
	public List<Menu> consultarPadres() {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		this.crearJoins(null);

		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();

		restricciones.add(
				this.criteriaBuilder.isNull(this.entity.get("padre"))
		);
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, 0, -1);
	}

	
}

package com.prueba.web.seguridad.configuracion.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.configuracion.dao.impl.ArbolDAOImpl;
import com.prueba.web.model.GroupMenu;
import com.prueba.web.seguridad.configuracion.dao.GroupMenuDAO;

@Repository
public class GroupMenuDAOImpl extends ArbolDAOImpl<GroupMenu> implements GroupMenuDAO {

	public GroupMenuDAOImpl() {
		super(GroupMenu.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<GroupMenu> consultarPadresMenuAsignadoGrupo(int idGrupo, int start, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		Map<String, JoinType> entidades=new HashMap<String, JoinType>();
		entidades.put("group", JoinType.INNER);
		Map<String, Join>joins=crearJoins(entidades);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();

		restricciones.add(
				this.criteriaBuilder.isNull(this.entity.get("padre"))
		);
		
		restricciones.add(
				this.criteriaBuilder.equal(joins.get("group").get("id"), idGrupo)
		);
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}	
}
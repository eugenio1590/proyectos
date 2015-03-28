package com.prueba.web.seguridad.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.prueba.web.seguridad.dao.HistoryLoginDAO;
import com.prueba.web.model.HistoryLogin;
import com.prueba.web.dao.impl.AbstractJpaDao;

public class HistoryLoginDAOImpl extends AbstractJpaDao<HistoryLogin, Integer> implements HistoryLoginDAO {

	public HistoryLoginDAOImpl() {
		super(HistoryLogin.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HistoryLogin sessionNoTerminada(String username) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		Map<String, JoinType> entidades = new HashMap<String, JoinType>();
		entidades.put("usuario", JoinType.INNER);
		Map<String, Join> joins = this.crearJoins(entidades );
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		
		restricciones.add(this.criteriaBuilder.isNull(this.entity.get("dateLogout")));
		
		if(username!=null)
			restricciones.add(this.criteriaBuilder.equal(
					this.criteriaBuilder.lower(joins.get("usuario").get("username")), username.toLowerCase()
			));
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		
		List<HistoryLogin> histories = this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, 0, -1);
		if(histories.size()>0)
			return histories.get(0);
		else 
			return null;
	}

	

}

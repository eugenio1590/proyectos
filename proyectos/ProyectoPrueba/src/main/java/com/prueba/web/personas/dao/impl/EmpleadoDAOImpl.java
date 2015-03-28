package com.prueba.web.personas.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.personas.dao.EmpleadoDAO;
import com.prueba.web.model.Empleado;

@Repository
public class EmpleadoDAOImpl extends AbstractJpaDao<Empleado, Integer> implements EmpleadoDAO {

	public EmpleadoDAOImpl() {
		super(Empleado.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Empleado> consultarEmpleadosSinUsuario(int start, int limit) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		Map<String, JoinType> entidades=new HashMap<String, JoinType>();
		entidades.put("usuario", JoinType.LEFT);
		Map<String, Join> joins = this.crearJoins(entidades);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		restricciones.add(this.criteriaBuilder.isNull(joins.get("usuario")));
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("idEmpleado", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}

	

}

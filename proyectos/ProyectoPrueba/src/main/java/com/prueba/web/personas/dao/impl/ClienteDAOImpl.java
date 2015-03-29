package com.prueba.web.personas.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.personas.dao.ClienteDAO;
import com.prueba.web.model.Cliente;
import com.prueba.web.model.Persona;

public class ClienteDAOImpl extends AbstractJpaDao<Cliente, Integer> implements ClienteDAO {
	
	public ClienteDAOImpl() {
		super(Cliente.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Cliente> consultarClientesSinUsuario(Persona clienteF, int start, int limit) {
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
		
		if(clienteF.getCedula()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("cedula")), 
					"%"+clienteF.getCedula().toLowerCase()+"%"
			));
		
		if(clienteF.getNombre()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("nombre")), 
					"%"+clienteF.getNombre().toLowerCase()+"%"
			));
		
		if(clienteF.getApellido()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("apellido")), 
					"%"+clienteF.getApellido().toLowerCase()+"%"
			));

		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("idCliente", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}

}

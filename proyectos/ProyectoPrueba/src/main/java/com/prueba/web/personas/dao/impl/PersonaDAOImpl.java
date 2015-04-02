package com.prueba.web.personas.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.Persona;
import com.prueba.web.personas.dao.PersonaDAO;

@Repository
public abstract class PersonaDAOImpl<T extends Persona> extends AbstractJpaDao<T, Integer> implements PersonaDAO<T> {

	public PersonaDAOImpl() {
		super(Persona.class);
		// TODO Auto-generated constructor stub
	}
	
	public PersonaDAOImpl(Class<? extends Persona> classz) {
		super(classz);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public T consultarPersona(T personaF){
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();
		
		//2. Generamos los Joins
		this.crearJoins(null);
		
		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
		
		agregarFiltros(personaF, restricciones);
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("id", true);
		
		List<T> personas = this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, 0, -1);
		if(personas.size()>0)
			return personas.get(0);
		else
			return null;
	}
	
	@Override
	public List<T> consultarPersonaSinUsuarios(T persona, String fieldSort, Boolean sortDirection, int start, int limit) {
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
		agregarFiltros(persona, restricciones);

		//4. Creamos los campos de ordenamiento y ejecutamos
		List<Order> orders = new ArrayList<Order>();
		
		if(fieldSort!=null && sortDirection!=null){
			Path<Object> field = this.entity.get(fieldSort);
			orders.add((sortDirection) ? this.criteriaBuilder.asc(this.entity.get(fieldSort)) : this.criteriaBuilder.desc(field));
		}else
			orders.add( this.criteriaBuilder.asc(this.entity.get("id")));
		
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, start, limit);
	}

	/**METODOS PROPIOS DE LA CLASE*/
	protected void agregarFiltros(T personaF, List<Predicate> restricciones){
		if(personaF!=null) {
		if(personaF.getCedula()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("cedula")), 
					"%"+personaF.getCedula().toLowerCase()+"%"
			));

		if(personaF.getNombre()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("nombre")), 
					"%"+personaF.getNombre().toLowerCase()+"%"
			));

		if(personaF.getApellido()!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("apellido")), 
					"%"+personaF.getApellido().toLowerCase()+"%"
			));
		}
		
		agregarRestriccionesPersona(personaF, restricciones);
	}
	
	protected abstract void agregarRestriccionesPersona(T persona, List<Predicate> restricciones);
}

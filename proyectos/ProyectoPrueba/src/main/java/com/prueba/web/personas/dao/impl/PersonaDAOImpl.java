package com.prueba.web.personas.dao.impl;

import java.util.List;

import javax.persistence.criteria.Predicate;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.Persona;
import com.prueba.web.personas.dao.PersonaDAO;

public abstract class PersonaDAOImpl<T extends Persona> extends AbstractJpaDao<T, Integer> implements PersonaDAO<T> {

	public PersonaDAOImpl() {
		super(Persona.class);
		// TODO Auto-generated constructor stub
	}
	
	public PersonaDAOImpl(Class<? extends Persona> classz) {
		super(classz);
		// TODO Auto-generated constructor stub
	}

	/**METODOS PROPIOS DE LA CLASE*/
	protected void agregarFiltros(Persona clienteF, List<Predicate> restricciones){
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
	}
}

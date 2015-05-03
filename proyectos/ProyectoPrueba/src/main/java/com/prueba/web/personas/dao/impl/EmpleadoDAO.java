package com.prueba.web.personas.dao.impl;

import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.prueba.web.model.Empleado;
import com.prueba.web.model.Persona;

public class EmpleadoDAO extends PersonaDAO<Empleado> {
	
	public Specification<Empleado> consultarEmpleadosSinUsuario(Persona empleadoF, 
			String fieldSort, Boolean sortDirection) {
		// TODO Auto-generated method stub
		return super.consultarPersonaSinUsuarios(new Empleado(empleadoF), fieldSort, sortDirection);
	}
	
	/**METODOS OVERRIDE*/
	@Override
	protected Expression<Boolean> agregarRestriccionesPersona(Empleado persona, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		Predicate p = criteriaBuilder.conjunction();
		return p;
	}

}

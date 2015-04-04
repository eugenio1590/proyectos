package com.prueba.web.personas.dao.impl;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.personas.dao.EmpleadoDAO;
import com.prueba.web.model.Empleado;
import com.prueba.web.model.Persona;

@Repository
public class EmpleadoDAOImpl extends PersonaDAOImpl<Empleado> implements EmpleadoDAO {

	public EmpleadoDAOImpl() {
		super(Empleado.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Empleado> consultarEmpleadosSinUsuario(Persona empleadoF, String fieldSort, Boolean sortDirection,
			int start, int limit) {
		// TODO Auto-generated method stub
		return super.consultarPersonaSinUsuarios(new Empleado(empleadoF), fieldSort, sortDirection, start, limit);
	}
	
	/**METODOS OVERRIDE*/
	@Override
	protected void agregarRestriccionesPersona(Empleado persona, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	@Override
	protected void agregarFiltros(Empleado empleadoF, List<Predicate> restricciones){
		if(empleadoF!=null){
			super.agregarFiltros(empleadoF, restricciones);
		}
	}

}

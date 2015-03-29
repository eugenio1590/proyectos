package com.prueba.web.personas.dao;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Empleado;
import com.prueba.web.model.Persona;

public interface EmpleadoDAO extends IGenericDao<Empleado, Integer> {
	public List<Empleado> consultarEmpleadosSinUsuario(Persona empleadoF, int start, int limit);
}

package com.prueba.web.personas.dao;

import java.util.List;

import com.prueba.web.model.Empleado;
import com.prueba.web.model.Persona;

public interface EmpleadoDAO extends PersonaDAO<Empleado> {
	public List<Empleado> consultarEmpleadosSinUsuario(Persona empleadoF, int start, int limit);
}

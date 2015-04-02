package com.prueba.web.personas.dao;

import java.util.List;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Persona;

public interface PersonaDAO<T extends Persona> extends IGenericDao<T, Integer> {
	public T consultarPersona(T personaF);
	public List<T> consultarPersonaSinUsuarios(T persona, String fieldSort, Boolean sortDirection, int start, int limit);
}

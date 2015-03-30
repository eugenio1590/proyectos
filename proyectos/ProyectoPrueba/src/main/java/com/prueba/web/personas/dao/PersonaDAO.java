package com.prueba.web.personas.dao;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.Persona;

public interface PersonaDAO<T extends Persona> extends IGenericDao<T, Integer> {

}

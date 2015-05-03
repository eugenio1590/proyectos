package com.prueba.web.personas.dao;

import org.springframework.data.repository.NoRepositoryBean;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.Persona;

@NoRepositoryBean
public interface PersonaRepository<T extends Persona> extends IGenericJPARepository<T, Integer> {

}

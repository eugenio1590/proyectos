package com.prueba.web.seguridad.configuracion.dao;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.Group;

@Repository
public interface GrupoRepository extends IGenericJPARepository<Group, Integer> {

}

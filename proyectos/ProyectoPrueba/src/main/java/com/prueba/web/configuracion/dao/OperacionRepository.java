package com.prueba.web.configuracion.dao;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.Operacion;

@Repository
public interface OperacionRepository extends IGenericJPARepository<Operacion, Integer> {

}

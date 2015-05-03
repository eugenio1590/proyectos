package com.prueba.web.configuracion.dao;

import org.springframework.data.repository.NoRepositoryBean;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.Arbol;

@NoRepositoryBean
public interface ArbolRepository<T extends Arbol> extends IGenericJPARepository<T, Integer>  {

}

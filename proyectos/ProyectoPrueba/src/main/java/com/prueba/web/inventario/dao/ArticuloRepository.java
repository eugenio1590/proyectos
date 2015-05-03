package com.prueba.web.inventario.dao;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.Articulo;

@Repository
public interface ArticuloRepository extends IGenericJPARepository<Articulo, Integer> {
	
}

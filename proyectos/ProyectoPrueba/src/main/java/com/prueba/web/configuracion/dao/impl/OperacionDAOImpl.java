package com.prueba.web.configuracion.dao.impl;

import com.prueba.web.configuracion.dao.OperacionDAO;
import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.model.Operacion;

public class OperacionDAOImpl extends AbstractJpaDao<Operacion, Integer> implements OperacionDAO {

	public OperacionDAOImpl() {
		super(Operacion.class);
		// TODO Auto-generated constructor stub
	}

	
}

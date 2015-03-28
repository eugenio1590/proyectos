package com.prueba.web.seguridad.dao;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.HistoryLogin;

public interface HistoryLoginDAO extends IGenericDao<HistoryLogin, Integer> {
	public HistoryLogin sessionNoTerminada(String username);
}

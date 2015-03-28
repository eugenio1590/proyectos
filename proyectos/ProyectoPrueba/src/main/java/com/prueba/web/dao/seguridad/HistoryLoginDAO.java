package com.prueba.web.dao.seguridad;

import com.prueba.web.dao.IGenericDao;
import com.prueba.web.model.HistoryLogin;

public interface HistoryLoginDAO extends IGenericDao<HistoryLogin, Integer> {
	public HistoryLogin sessionNoTerminada(String username);
}

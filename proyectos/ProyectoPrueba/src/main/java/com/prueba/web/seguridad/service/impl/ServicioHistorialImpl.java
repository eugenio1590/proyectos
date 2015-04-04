package com.prueba.web.seguridad.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.web.seguridad.dao.HistoryLoginDAO;
import com.prueba.web.model.HistoryLogin;
import com.prueba.web.model.Usuario;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.service.impl.AbstractServiceImpl;
import com.prueba.web.seguridad.service.ServicioHistorial;

@Service
public class ServicioHistorialImpl extends AbstractServiceImpl implements ServicioHistorial {

	@Autowired
	@BeanInjector("historyLoginDAO")
	private HistoryLoginDAO historyLoginDAO;
	
	//1. Historial de Session
	@Override
	public void registrarHistorialSession(Usuario usuario){

		HistoryLogin history = this.historyLoginDAO.sessionNoTerminada(usuario.getUsername());
		if(history==null){
			history = new HistoryLogin();
			history.setUsuario(usuario);
			history.setDateLogin(new Timestamp(calendar.getTime().getTime()));
			this.historyLoginDAO.save(history);
		}
	}

	@Override
	public void cerarHistorialSession(Usuario usuario){
		HistoryLogin history = this.historyLoginDAO.sessionNoTerminada(usuario.getUsername());
		if(history!=null){
			history.setDateLogout(new Timestamp(calendar.getTime().getTime()));
			this.historyLoginDAO.update(history);
		}
	}
	
	/**SETTERS Y GETTERS*/
	public HistoryLoginDAO getHistoryLoginDAO() {
		return historyLoginDAO;
	}

	public void setHistoryLoginDAO(HistoryLoginDAO historyLoginDAO) {
		this.historyLoginDAO = historyLoginDAO;
	}

}

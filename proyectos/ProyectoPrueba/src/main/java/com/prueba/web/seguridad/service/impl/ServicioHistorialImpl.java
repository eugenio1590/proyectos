package com.prueba.web.seguridad.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.web.seguridad.dao.HistoryLoginRepository;
import com.prueba.web.model.HistoryLogin;
import com.prueba.web.model.Usuario;
import com.prueba.web.service.impl.AbstractServiceImpl;
import com.prueba.web.seguridad.service.ServicioHistorial;

@Service
public class ServicioHistorialImpl extends AbstractServiceImpl implements ServicioHistorial {
	
	@Autowired
	private HistoryLoginRepository historyLoginRepository;
	
	//1. Historial de Session
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void registrarHistorialSession(Usuario usuario){
		HistoryLogin history = historyLoginRepository.findByDateLogoutIsNullAndUsuarioId(usuario.getId());
		if(history==null){
			history = new HistoryLogin();
			history.setUsuario(usuario);
			history.setDateLogin(new Timestamp(calendar.getTime().getTime()));
			this.historyLoginRepository.save(history);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void cerarHistorialSession(Usuario usuario){
		HistoryLogin history = historyLoginRepository.findByDateLogoutIsNullAndUsuarioId(usuario.getId());
		if(history!=null){
			history.setDateLogout(new Timestamp(calendar.getTime().getTime()));
			this.historyLoginRepository.save(history);
		}
	}

}

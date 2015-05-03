package com.prueba.web.seguridad.dao;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.HistoryLogin;

@Repository
public interface HistoryLoginRepository extends IGenericJPARepository<HistoryLogin, Integer> {
	HistoryLogin findByDateLogoutIsNullAndUsuarioId(Integer idUsuario);
}

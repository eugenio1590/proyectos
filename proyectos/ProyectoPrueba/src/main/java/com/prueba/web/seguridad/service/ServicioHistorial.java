package com.prueba.web.seguridad.service;

import com.prueba.web.model.Usuario;

public interface ServicioHistorial {
	//Historial de Session
	public void registrarHistorialSession(Usuario usuario);
	public void cerarHistorialSession(Usuario usuario);
}

package com.prueba.web.service.seguridad;

import com.prueba.web.model.Usuario;

public interface ServicioHistorial {
	//Historial de Session
	public void registrarHistorialSession(Usuario usuario);
	public void cerarHistorialSession(Usuario usuario);
}

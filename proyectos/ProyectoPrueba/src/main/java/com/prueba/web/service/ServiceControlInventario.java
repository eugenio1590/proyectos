package com.prueba.web.service;

import java.util.Map;

public interface ServiceControlInventario {
	public Map<String, Object> listarArticulos(int pagina, int limit);
	public Map<String, Object> consultarArticulosCodigoONombre(String codigo, String nombre, int pagina, int limit);
}

package com.prueba.web.personas.dao;

import java.util.List;

import com.prueba.web.model.Cliente;
import com.prueba.web.model.Persona;

public interface ClienteDAO extends PersonaDAO<Cliente> {
	public List<Cliente> consultarClientesSinUsuario(Persona clienteF, int start, int limit);
}

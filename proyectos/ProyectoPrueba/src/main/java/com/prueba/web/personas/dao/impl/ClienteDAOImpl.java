package com.prueba.web.personas.dao.impl;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.personas.dao.ClienteDAO;
import com.prueba.web.model.Cliente;
import com.prueba.web.model.Persona;

@Repository
public class ClienteDAOImpl extends PersonaDAOImpl<Cliente> implements ClienteDAO {
	
	public ClienteDAOImpl() {
		super(Cliente.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Cliente> consultarClientesSinUsuario(Persona clienteF, String fieldSort, Boolean sortDirection, 
			int start, int limit) {
		// TODO Auto-generated method stub
		return super.consultarPersonaSinUsuarios(new Cliente(clienteF), fieldSort, sortDirection, start, limit);
	}
	
	/**METODOS OVERRIDE*/
	@Override
	protected void agregarRestriccionesPersona(Cliente persona, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	@Override
	protected void agregarFiltros(Cliente clienteF, List<Predicate> restricciones){
		if(clienteF!=null){
			super.agregarFiltros(clienteF, restricciones);
		}
	}
}

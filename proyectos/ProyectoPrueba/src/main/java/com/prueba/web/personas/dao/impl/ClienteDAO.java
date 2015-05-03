package com.prueba.web.personas.dao.impl;

import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.prueba.web.model.Cliente;
import com.prueba.web.model.Persona;

public class ClienteDAO extends PersonaDAO<Cliente> {
	
	public Specification<Cliente> consultarClientesSinUsuario(Persona clienteF, 
			String fieldSort, Boolean sortDirection) {
		// TODO Auto-generated method stub
		return super.consultarPersonaSinUsuarios(new Cliente(clienteF), fieldSort, sortDirection);
	}
	
	/**METODOS OVERRIDE*/
	@Override
	protected Expression<Boolean> agregarRestriccionesPersona(Cliente persona, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		Predicate p = criteriaBuilder.conjunction();
		return p;
	}
}

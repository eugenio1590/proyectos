package com.prueba.web.personas.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.prueba.web.dao.AbstractJpaDao;
import com.prueba.web.model.Persona;

public abstract class PersonaDAO<T extends Persona> extends AbstractJpaDao<T> {
	
	public Specification<T> consultarPersonaSinUsuarios(final T persona, 
			final String fieldSort, final Boolean sortDirection) {
		// TODO Auto-generated method stub
		return new Specification<T>() {

			@Override
			public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Creamos las Restricciones de la busqueda
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("usuario", JoinType.LEFT);
				Map<String, Join> joins = crearJoins(entidades);
				
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(criteriaBuilder.isNull(joins.get("usuario")));
				Predicate p = agregarFiltros(persona, restricciones);
				
				//4. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				
				if(fieldSort!=null && sortDirection!=null)
					agregarOrder(fieldSort, sortDirection, orders);
				else
					agregarOrder("id", true, orders);
				
				return criteriaBuilder.and(p, crearPredicate(restricciones));
			}
		};
	}

	/**METODOS PROPIOS DE LA CLASE
	 * @return */
	protected Predicate agregarFiltros(T personaF, List<Predicate> restricciones){
		Predicate p = constructExample(personaF, entity, criteriaQuery, criteriaBuilder);
		return criteriaBuilder.and(p, agregarRestriccionesPersona(personaF, restricciones));
	}
	
	protected abstract Expression<Boolean> agregarRestriccionesPersona(T persona, List<Predicate> restricciones);
}

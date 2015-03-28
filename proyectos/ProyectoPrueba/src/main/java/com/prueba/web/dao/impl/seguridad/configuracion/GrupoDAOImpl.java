package com.prueba.web.dao.impl.seguridad.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.impl.AbstractJpaDao;
import com.prueba.web.dao.seguridad.configuracion.GrupoDAO;
import com.prueba.web.model.Group;

@Repository
public class GrupoDAOImpl extends AbstractJpaDao<Group, Integer> implements GrupoDAO {

	public GrupoDAOImpl() {
		super(Group.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Group> consultarGrupos(String nombre, String fieldSort, Boolean sortDirection, int pagina, int limite) {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		this.crearJoins(null);

		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();
				
		if(nombre!=null)
			restricciones.add(this.criteriaBuilder.like(
					this.criteriaBuilder.lower(this.entity.<String>get("groupName")), nombre.toLowerCase()
			));

		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		
		if(fieldSort!=null && sortDirection!=null)
			orders.put(fieldSort, sortDirection);
				
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, pagina, limite);
	}

	
}

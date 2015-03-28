package com.prueba.web.dao.impl.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.configuracion.MenuDAO;
import com.prueba.web.model.Menu;
import com.prueba.web.dao.impl.AbstractJpaDao;

@Repository
public class MenuDAOImpl extends AbstractJpaDao<Menu, Integer> implements MenuDAO {

	public MenuDAOImpl() {
		super(Menu.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Menu> consultarPadres() {
		// TODO Auto-generated method stub
		//1. Creamos el Criterio de busqueda
		this.crearCriteria();

		//2. Generamos los Joins
		this.crearJoins(null);

		//3. Creamos las Restricciones de la busqueda
		List<Predicate> restricciones = new ArrayList<Predicate>();

		restricciones.add(
				this.criteriaBuilder.isNull(this.entity.get("padre"))
		);
		
		//4. Creamos los campos de ordenamiento y ejecutamos
		Map<String, Boolean> orders = new HashMap<String, Boolean>();
		orders.put("idMenu", true);
		return this.ejecutarCriteria(concatenaArrayPredicate(restricciones), orders, 0, -1);
	}

	

}

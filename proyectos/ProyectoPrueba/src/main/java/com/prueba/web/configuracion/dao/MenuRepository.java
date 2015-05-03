package com.prueba.web.configuracion.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.prueba.web.model.Menu;

@Repository
public interface MenuRepository extends ArbolRepository<Menu> {
	Page<Menu> findByPadreIsNull(Pageable pageable);
}

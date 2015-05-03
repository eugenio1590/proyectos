package com.prueba.web.seguridad.configuracion.dao;

import org.springframework.stereotype.Repository;

import com.prueba.web.configuracion.dao.ArbolRepository;
import com.prueba.web.model.GroupMenu;

@Repository
public interface GroupMenuRepository extends ArbolRepository<GroupMenu> {

}

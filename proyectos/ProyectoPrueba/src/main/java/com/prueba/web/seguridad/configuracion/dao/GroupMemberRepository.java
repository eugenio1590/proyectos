package com.prueba.web.seguridad.configuracion.dao;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.GroupMember;

@Repository
public interface GroupMemberRepository extends IGenericJPARepository<GroupMember, Integer> {

}

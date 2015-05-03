package com.prueba.web.configuracion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prueba.web.dao.IGenericJPARepository;
import com.prueba.web.model.Usuario;

@Repository
public interface UsuarioRepository extends IGenericJPARepository<Usuario, Integer> {
	Usuario findById(Integer id);
	List<Usuario> findByUsernameContainingIgnoreCaseOrPaswordContainingIgnoreCase(
			String username, String pasword);
}

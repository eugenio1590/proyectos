package com.prueba.web.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericJPARepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
	public Page<T> findAll(Specification<T> specification, Pageable pageable);
	public List<T> findAll(Specification<T> specification);
}

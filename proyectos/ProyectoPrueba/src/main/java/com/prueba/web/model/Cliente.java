package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Table(name="cliente")
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generated(GenerationTime.INSERT)
	@Column(name="id_cliente",  columnDefinition = "serial")
	private Integer idCliente;
	
	@OneToOne(mappedBy="cliente")
	private Usuario usuario;

	public Cliente() {
		// TODO Auto-generated constructor stub
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

}

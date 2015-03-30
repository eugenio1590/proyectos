package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Table(name="cliente")
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
@AttributeOverride(name="id", column=@Column(name="id_cliente"))
public class Cliente extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@OneToOne(mappedBy="cliente")
	private Usuario usuario;

	public Cliente() {
		// TODO Auto-generated constructor stub
	}

}

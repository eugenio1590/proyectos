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
@PrimaryKeyJoinColumn(name="id_cliente", columnDefinition="integer")
public class Cliente extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;

	public Cliente() {
		// TODO Auto-generated constructor stub
	}
	
	public Cliente(Persona persona) {
		super(persona);
	}

}

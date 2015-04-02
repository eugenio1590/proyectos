package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the empleado database table.
 * 
 */
@Entity
@Table(name="empleado")
@NamedQuery(name="Empleado.findAll", query="SELECT e FROM Empleado e")
@PrimaryKeyJoinColumn(name="id_empleado", columnDefinition="integer")
public class Empleado extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Empleado() {
	}
	
	public Empleado(Persona persona) {
		super(persona);
	}
}

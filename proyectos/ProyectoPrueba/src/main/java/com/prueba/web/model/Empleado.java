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
@AttributeOverride(name="id", column=@Column(name="id_empleado"))
public class Empleado extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@OneToOne(mappedBy="empleado")
	private Usuario usuario;
	
	public Empleado() {
	}
}

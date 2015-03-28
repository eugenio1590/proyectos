package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 * The persistent class for the empleado database table.
 * 
 */
@Entity
@Table(name="empleado")
@NamedQuery(name="Empleado.findAll", query="SELECT e FROM Empleado e")
public class Empleado extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generated(GenerationTime.INSERT)
	@Column(name="id_empleado", columnDefinition = "serial")
	private Integer idEmpleado;
	
	@OneToOne(mappedBy="empleado")
	private Usuario usuario;
	
	public Empleado() {
	}

	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

}

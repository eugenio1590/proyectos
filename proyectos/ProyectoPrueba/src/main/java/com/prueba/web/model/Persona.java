package com.prueba.web.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the persona database table.
 * 
 */
@MappedSuperclass
public abstract class Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(unique=true, nullable=false)
	private String cedula;

	@Column(length=50)
	private String apellido;

	@Column(length=20)
	private String correo;
	
	@Column
	private String direccion;

	@Column(length=50)
	private String nombre;

	@Column
	private Boolean sexo;
	
	@Column(length=20)
	private String telefono;

	public Persona() {
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getSexo() {
		return this.sexo;
	}

	public void setSexo(Boolean sexo) {
		this.sexo = sexo;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
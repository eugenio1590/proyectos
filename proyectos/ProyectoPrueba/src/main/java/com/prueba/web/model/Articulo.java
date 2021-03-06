package com.prueba.web.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the articulo database table.
 * 
 */
@Entity
@Table(name="articulo")
@NamedQuery(name="Articulo.findAll", query="SELECT a FROM Articulo a")
public class Articulo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=50)
	private String categoria;

	@Column(length=10)
	private String codigo;

	@Column(precision=1000)
	private BigDecimal costo;

	@Column(precision=1000)
	private BigDecimal existencia;

	@Column(length=100)
	private String nombre;

	@Column(precision=1000)
	private BigDecimal precio;

	public Articulo() {
	}
	
	public Articulo(String codigo, String nombre){
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getCosto() {
		return this.costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public BigDecimal getExistencia() {
		return this.existencia;
	}

	public void setExistencia(BigDecimal existencia) {
		this.existencia = existencia;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

}
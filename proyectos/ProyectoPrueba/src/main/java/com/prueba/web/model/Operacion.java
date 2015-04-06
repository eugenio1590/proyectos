package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the operacion database table.
 * 
 */
@Entity
@Table(name="operacion")
@JsonIgnoreProperties({"accesos", "menus"})
public class Operacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_operacion", columnDefinition="serial")
	private Integer idOperacion;

	@Column(name="nombre")
	private String nombre;

	//bi-directional many-to-many association to Menu
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(
			name="menu_operacion"
			, joinColumns={
				@JoinColumn(name="id_operacion", columnDefinition="integer")
				}
			, inverseJoinColumns={
				@JoinColumn(name="id_menu", columnDefinition="integer")
				}
			)
	private List<Menu> menus;

	//bi-directional many-to-many association to GroupMenu
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(
		name="group_menu_operacion"
		, joinColumns={
			@JoinColumn(name="id_operacion", columnDefinition="integer")
			}
		, inverseJoinColumns={	
			@JoinColumn(name="id_group_menu", columnDefinition="integer")
			}
		)
	private List<GroupMenu> groupMenus;

	public Operacion() {
		this.groupMenus = new ArrayList<GroupMenu>();
	}

	public Integer getIdOperacion() {
		return this.idOperacion;
	}

	public void setIdOperacion(Integer idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
	public List<GroupMenu> getGroupMenus() {
		return groupMenus;
	}

	public void setGroupMenus(List<GroupMenu> groupMenus) {
		this.groupMenus = groupMenus;
	}

}
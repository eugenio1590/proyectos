package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import com.prueba.web.mvvm.ModelNavbar;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name="menu")
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
public class Menu implements Serializable, ModelNavbar{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_menu", unique=true, nullable=false)
	private Integer idMenu;

	@Column(length=255)
	private String actividad;

	@Column(length=255)
	private String icono;

	@Column(length=255)
	private String nombre;

	@Column(length=255)
	private String ruta;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="id_padre")
	private Menu padre;

	//bi-directional many-to-one association to Menu
	@OneToMany(mappedBy="padre")
	private List<Menu> hijos;

	public Menu() {
	}

	public Integer getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}

	public String getActividad() {
		return this.actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public Menu getPadre() {
		return this.padre;
	}

	public void setPadre(Menu menu) {
		this.padre = menu;
	}

	public List<Menu> getHijos() {
		return this.hijos;
	}

	public void setHijos(List<Menu> hijos) {
		this.hijos = hijos;
	}

	public Menu addHijo(Menu menu) {
		getHijos().add(menu);
		menu.setPadre(this);

		return menu;
	}

	public Menu removeHijo(Menu menu) {
		getHijos().remove(menu);
		menu.setPadre(null);

		return menu;
	}

	/**INTERFAZ*/
	//1.ModelNavbar
	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.getNombre();
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return this.getIcono();
	}

	@Override
	public String getUriLocation() {
		// TODO Auto-generated method stub
		return this.getRuta();
	}

	@Override
	public List<ModelNavbar> getChilds() {
		// TODO Auto-generated method stub
		List<ModelNavbar> temp = new ArrayList<ModelNavbar>();
		if(this.hijos!=null)
			for(Menu menu : this.getHijos())
				temp.add(menu);
		return temp;
	}

}
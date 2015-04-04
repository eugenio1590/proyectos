package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name="menu")
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
@PrimaryKeyJoinColumn(name="id_menu", columnDefinition="integer")
public class Menu extends Arbol implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(length=255)
	private String actividad;

	@Column(length=255)
	private String icono;

	@Column(length=255)
	private String nombre;

	@Column(length=255)
	private String ruta;
	
	//bi-directional many-to-one association to GroupMenu
	@OneToMany(mappedBy="menu", fetch=FetchType.LAZY)
	private List<GroupMenu> groupMenus;

	public Menu() {
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
	
	public List<GroupMenu> getGroupMenus() {
		return groupMenus;
	}

	public void setGroupMenus(List<GroupMenu> groupMenus) {
		this.groupMenus = groupMenus;
	}
	
	/**METODOS OVERRIDE*/
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

	/**METODOS PROPIOS DE LA CLASE*/
	public Menu getParentRoot(){
		Menu rootParent=(Menu) padre;
		while(rootParent!=null)
			rootParent = (Menu) rootParent.padre;
		return rootParent;
	}
	
	public Boolean isRootParent(){
		return (this.padre==null);
	}
	
	//Hijos
	public Menu getParent(Integer id){
		Menu parent=(Menu) padre;
		if(parent!=null)
			while(parent.getId()!=id && parent.getPadre()!=null)
				parent = (Menu) parent.padre;
		else
			parent = this;
		return parent;
	}
}
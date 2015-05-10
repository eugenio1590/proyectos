package com.prueba.web.model;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.persistence.*;

import org.hibernate.Hibernate;

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
@PrimaryKeyJoinColumn(name="id_menu")
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
	
	//bi-directional many-to-many association to Operacion
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(
		name="menu_operacion"
		, joinColumns={
			@JoinColumn(name="id_menu")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_operacion")
			}
		)
	private List<Operacion> operacions;

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
	
	public List<Operacion> getOperacions() {
		return operacions;
	}

	public void setOperacions(List<Operacion> operacions) {
		this.operacions = operacions;
	}
	
	/**EVENTOS*/
	@Override
	@PostLoad
	public void postLoad(){
		super.postLoad();
		try {
			Hibernate.initialize(this.operacions);
		}
		catch (Exception e){
			return;
		}
	}
	
	@PostConstruct
	public void postConstruct(){
		try {
			Hibernate.initialize(this.operacions);
		}
		catch (Exception e){
			return;
		}
	}

	/**METODOS OVERRIDE*/
	@Override
	public int getIdNode(){
		return this.getId();
	}
	
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
	public List<ModelNavbar> getRootTree() {
		// TODO Auto-generated method stub
		List<ModelNavbar> rootTree = new ArrayList<ModelNavbar>();
		calculateRootTree(rootTree, this);
		return rootTree;
	}

	/**METODOS PROPIOS DE LA CLASE*/
	public Menu getParentRoot(){
		Menu rootParent=(Menu) padre;
		while(rootParent!=null)
			rootParent = (Menu) rootParent.padre;
		return rootParent;
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
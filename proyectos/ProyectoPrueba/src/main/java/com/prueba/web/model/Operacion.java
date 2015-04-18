package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="operacion_id_seq")
	@SequenceGenerator(name="operacion_id_seq", sequenceName="operacion_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_operacion")
	private Integer idOperacion;

	@Column(name="nombre")
	private String nombre;

	//bi-directional many-to-many association to Menu
	@ManyToMany(mappedBy="operacions", cascade={CascadeType.MERGE, CascadeType.PERSIST}, 
			fetch=FetchType.LAZY)
	private List<Menu> menus;

	//bi-directional many-to-many association to GroupMenu
	@ManyToMany(mappedBy="operacions", cascade={CascadeType.MERGE, CascadeType.PERSIST}, 
			fetch=FetchType.LAZY)
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
	
	/**METODOS ESTATICOS DE LA CLASE*/
	public static Comparator<Operacion> getComparator(){
		return new  Comparator<Operacion>() {

			@Override
			public int compare(Operacion operacion1, Operacion operacion2) {
				// TODO Auto-generated method stub
				return operacion1.getIdOperacion().compareTo(operacion2.getIdOperacion());
			}
		};
	}
	
	public static void ordenarListaOperacion(List<Operacion> operaciones){
		Collections.sort(operaciones, getComparator());
	}

}
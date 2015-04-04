package com.prueba.web.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.prueba.web.mvvm.ModelNavbar;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Arbol implements ModelNavbar {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false, columnDefinition="serial")
	protected Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_padre", columnDefinition="integer")
	protected Arbol padre;
	
	@OneToMany(mappedBy="padre", fetch=FetchType.LAZY)
	protected List<Arbol> hijos;

	public Arbol() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Arbol getPadre() {
		return padre;
	}

	public void setPadre(Arbol padre) {
		this.padre = padre;
	}

	public List<Arbol> getHijos() {
		return hijos;
	}

	public void setHijos(List<Arbol> hijos) {
		this.hijos = hijos;
	}

	/**INTERFAZ*/
	//1. ModelNavbar
	@Override
	public List<ModelNavbar> getChilds() {
		// TODO Auto-generated method stub
		List<ModelNavbar> temp = new ArrayList<ModelNavbar>();
		if(this.hijos!=null)
			for(Arbol hijo : this.getHijos())
				temp.add(hijo);
		return temp;
	}

	@Override
	public <T> T[] childToArray(Class<?> clazz) {
		// TODO Auto-generated method stub
		return childToArray(clazz, this.hijos.size());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] childToArray(Class<?> clazz, int element) {
		// TODO Auto-generated method stub
		return getChilds().toArray( (T[]) Array.newInstance(clazz, element));
	}

}

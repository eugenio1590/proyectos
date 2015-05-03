package com.prueba.web.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.*;

import com.prueba.web.mvvm.ModelNavbar;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Arbol implements ModelNavbar {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="arbol_id_seq")
	@SequenceGenerator(name="arbol_id_seq", sequenceName="arbol_id_seq", initialValue=1, allocationSize=1)
	@Column(unique=true, nullable=false)
	protected Integer id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_padre")
	protected Arbol padre;
	
	@OneToMany(mappedBy="padre", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
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
	public void setParent(ModelNavbar parent) {
		// TODO Auto-generated method stub
		this.setPadre((Arbol) parent);
	}
	
	@Override
	public ModelNavbar getParent(){
		return this.getPadre();
	}
	
	@Override
	public Boolean isRootParent(){
		return (this.getPadre()==null);
	}
	
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
	
	/**METODOS PROPIOS DE LA CLASE*/
	protected void calculateRootTree(List<ModelNavbar> root, ModelNavbar nodo){
		if(nodo.isRootParent())
			root.add(nodo);
		else {
			calculateRootTree(root, nodo.getParent());
			root.add(nodo);
		}
	}
	
	/**METODOS ESTATICOS DE LA CLASE*/
	public static Comparator<Arbol> getComparator(){
		return new Comparator<Arbol>() {
			@Override
			public int compare(Arbol a1, Arbol a2) {
				// TODO Auto-generated method stub
				return a1.getId().compareTo(a2.getId());
			}
		};
	}
	
	public static void ordenarListaArbol(List<? extends Arbol> listaArbol){
		Collections.sort(listaArbol, getComparator());
	}
}

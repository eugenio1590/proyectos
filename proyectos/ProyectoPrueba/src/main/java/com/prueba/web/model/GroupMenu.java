package com.prueba.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.*;

import org.hibernate.Hibernate;

import com.prueba.web.mvvm.ModelNavbar;

@Entity
@Table(name="group_menu")
@NamedQuery(name="GroupMenu.findAll", query="SELECT g FROM GroupMenu g")
@PrimaryKeyJoinColumn(name="id_group_menu")
public class GroupMenu extends Arbol implements Serializable {
	private static final long serialVersionUID = 1L;
		
	//bi-directional many-to-one association to Menu
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="menu_id")
	private Menu menu;
	
	//bi-directional many-to-one association to Group
	@ManyToOne
	@JoinColumn(name="group_id")
	private Group group;
	
	//bi-directional many-to-one association to Operacion
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(
		name="group_menu_operacion"
		, joinColumns={
			@JoinColumn(name="id_group_menu")
				}
		, inverseJoinColumns={	
			@JoinColumn(name="id_operacion")
			}
		)
	private List<Operacion> operacions;

	public GroupMenu() {
		this.operacions=new ArrayList<Operacion>();
	}
	
	public GroupMenu(Menu menu){
		this.menu = menu;
	}
	
	public GroupMenu(Menu menu, Object hijos){
		this.menu = menu;
		this.hijos = (List) hijos;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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
	/*@PostConstruct
	public void postConstruct(){
		try {
			Hibernate.initialize(this.operacions);
		}
		catch (Exception e){
			return;
		}
	}*/

	/**METODOS OVERRIDE*/
	@Override
	public int getIdNode(){
		return this.getMenu().getId();
	}
	
	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.getMenu().getNombre();
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return this.getMenu().getIcon();
	}

	@Override
	public String getUriLocation() {
		// TODO Auto-generated method stub
		return this.getMenu().getRuta();
	}
	
	@Override
	public List<ModelNavbar> getRootTree() {
		// TODO Auto-generated method stub
		List<ModelNavbar> rootTree = new ArrayList<ModelNavbar>();
		calculateRootTree(rootTree, (this.getPadre()==null) ? this.getMenu() : this);
		return rootTree;
	}
}

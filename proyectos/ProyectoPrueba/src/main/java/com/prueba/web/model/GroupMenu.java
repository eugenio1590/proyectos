package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="group_menu")
@NamedQuery(name="GroupMenu.findAll", query="SELECT g FROM GroupMenu g")
@PrimaryKeyJoinColumn(name="id_group_menu", columnDefinition="integer")
public class GroupMenu extends Arbol implements Serializable {
	private static final long serialVersionUID = 1L;
		
	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="menu_id", columnDefinition="integer")
	private Menu menu;
	
	//bi-directional many-to-one association to Group
	@ManyToOne
	@JoinColumn(name="group_id")
	private Group group;
	
	//Lista de Operaciones

	public GroupMenu() {
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

	/**METODOS OVERRIDE*/
	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.getMenu().getNombre();
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return this.getMenu().getNombre();
	}

	@Override
	public String getUriLocation() {
		// TODO Auto-generated method stub
		return this.getMenu().getRuta();
	}
}

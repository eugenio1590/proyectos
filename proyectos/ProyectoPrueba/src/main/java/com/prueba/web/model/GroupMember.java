package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the group_members database table.
 * 
 */
@Entity
@Table(name="group_members")
@NamedQuery(name="GroupMember.findAll", query="SELECT g FROM GroupMember g")
public class GroupMember implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to Group
	@ManyToOne
	@JoinColumn(name="group_id", nullable=false)
	private Group group;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="username", referencedColumnName="username", nullable=false)
	private Usuario usuario;

	public GroupMember() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
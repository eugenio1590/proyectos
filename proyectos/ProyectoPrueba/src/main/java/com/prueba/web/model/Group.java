package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name="groups")
@NamedQuery(name="Group.findAll", query="SELECT g FROM Group g")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="group_name", nullable=false, length=50)
	private String groupName;
	
	@Column(name="authority", nullable=false, length=100)
	private String authority;

	//bi-directional many-to-one association to GroupMember
	@OneToMany(mappedBy="group", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<GroupMember> groupMembers;
	
	//bi-directional many-to-one association to GroupMenu
	@OneToMany(mappedBy="group")
	private List<GroupMenu> groupMenus; 

	public Group() {
		groupMembers = new ArrayList<GroupMember>();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public List<GroupMember> getGroupMembers() {
		return this.groupMembers;
	}

	public void setGroupMembers(List<GroupMember> groupMembers) {
		this.groupMembers.clear();
		this.groupMembers.addAll(groupMembers);
	}

	public GroupMember addGroupMember(GroupMember groupMember) {
		getGroupMembers().add(groupMember);
		groupMember.setGroup(this);

		return groupMember;
	}

	public GroupMember removeGroupMember(GroupMember groupMember) {
		getGroupMembers().remove(groupMember);
		groupMember.setGroup(null);

		return groupMember;
	}

}
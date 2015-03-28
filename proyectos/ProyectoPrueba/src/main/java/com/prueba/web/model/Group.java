package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

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

	//bi-directional many-to-many association to Authority
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	@JoinTable(
		name="group_authorities"
		, joinColumns={
			@JoinColumn(name="group_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="authority_id", nullable=false)
			}
		)
	private List<Authority> authorities;

	//bi-directional many-to-one association to GroupMember
	@OneToMany(mappedBy="group", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	private List<GroupMember> groupMembers;

	public Group() {
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

	public List<Authority> getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public List<GroupMember> getGroupMembers() {
		return this.groupMembers;
	}

	public void setGroupMembers(List<GroupMember> groupMembers) {
		this.groupMembers = groupMembers;
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
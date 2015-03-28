package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.prueba.web.mvvm.AbstractViewModel;

import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name="usuario")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@Generated(GenerationTime.INSERT)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(columnDefinition="serial", unique=true, nullable=false, insertable=false, updatable=false)
	private Integer id;

	@Column
	@Basic(fetch=FetchType.LAZY)
	private byte[] foto;
	
	@Column(length=20, unique=true)
	private String username;

	@Column(length=100)
	private String pasword;
	
	@Column(nullable=false)
	private Boolean activo;

	//bi-directional many-to-one association to PersistentLogin
	@OneToMany(mappedBy="usuario",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<PersistentLogin> persistentLogins;
	
	//bi-directional many-to-one association to GroupMember
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<GroupMember> groupMembers;
	
	//bi-directional many-to-one association to HistoryLogin
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY)
	private List<HistoryLogin> historyLogins;
	
	/**USUARIOS ESPECIFICOS*/
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_empleado", referencedColumnName="id_empleado", columnDefinition="integer",
			nullable=true)
	private Empleado empleado;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_cliente", referencedColumnName="id_cliente", columnDefinition="integer", 
		nullable=true)
	private Cliente cliente;

	public Usuario() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasword() {
		return this.pasword;
	}

	public void setPasword(String pasword) {
		this.pasword = pasword;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public List<PersistentLogin> getPersistentLogins() {
		return this.persistentLogins;
	}

	public void setPersistentLogins(List<PersistentLogin> persistentLogins) {
		this.persistentLogins = persistentLogins;
	}

	public PersistentLogin addPersistentLogin(PersistentLogin persistentLogin) {
		getPersistentLogins().add(persistentLogin);
		persistentLogin.setUsuario(this);

		return persistentLogin;
	}

	public PersistentLogin removePersistentLogin(PersistentLogin persistentLogin) {
		getPersistentLogins().remove(persistentLogin);
		persistentLogin.setUsuario(null);

		return persistentLogin;
	}
	
	public List<GroupMember> getGroupMembers() {
		return this.groupMembers;
	}

	public void setGroupMembers(List<GroupMember> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public GroupMember addGroupMember(GroupMember groupMember) {
		getGroupMembers().add(groupMember);
		groupMember.setUsuario(this);

		return groupMember;
	}

	public GroupMember removeGroupMember(GroupMember groupMember) {
		getGroupMembers().remove(groupMember);
		groupMember.setUsuario(null);

		return groupMember;
	}
	
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**METODOS PROPIOS DE LA CLASE*/
	public String getFoto64(){
		if(this.foto!=null)
			return AbstractViewModel.decodificarImagen(foto);
		else
			return null;
	}
	
	public String isActivo(){
		return (this.activo) ? "Activo" : "Inactivo";
	}
	
	public String determinarTipo(){
		if(this.cliente!=null)
			return "Cliente";
		
		if(this.empleado!=null)
			return "Empleado";
		
		return "";
	}
	
	public void asignarUsuario(Persona persona){
		if(persona instanceof Empleado)
			this.setEmpleado((Empleado) persona);
		else if(persona instanceof Cliente)
			this.setCliente((Cliente) persona);
	}

}
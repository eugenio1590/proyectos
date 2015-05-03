package com.prueba.web.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the persona database table.
 * 
 */
@Entity
@Table(name="persona")
@Inheritance(strategy=InheritanceType.JOINED)
public class Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="persona_id_seq")
	@SequenceGenerator(name="persona_id_seq", sequenceName="persona_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(unique=true, nullable=false)
	private String cedula;

	@Column(length=50)
	private String apellido;

	@Column(length=20)
	private String correo;
	
	@Column
	private String direccion;

	@Column(length=50)
	private String nombre;

	@Column
	private Boolean sexo;
	
	@Column(length=20)
	private String telefono;
	
	//bi-directional many-to-one association to Usuario (Relacion Poliformica)
	@OneToOne(mappedBy="persona")
	private Usuario usuario;

	public Persona() {
	}
	
	public Persona(Persona persona){
		this(persona.getId(), persona.getCedula(), persona.getApellido(), persona.getCorreo(),
				persona.getDireccion(), persona.getNombre(), persona.getSexo(), persona.getTelefono(),
				persona.getUsuario());
	}
	
	public Persona(Integer id, String cedula, String apellido, String correo,
			String direccion, String nombre, Boolean sexo, String telefono,
			com.prueba.web.model.Usuario usuario) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.apellido = apellido;
		this.correo = correo;
		this.direccion = direccion;
		this.nombre = nombre;
		this.sexo = sexo;
		this.telefono = telefono;
		this.usuario = usuario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getSexo() {
		return this.sexo;
	}

	public void setSexo(Boolean sexo) {
		this.sexo = sexo;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
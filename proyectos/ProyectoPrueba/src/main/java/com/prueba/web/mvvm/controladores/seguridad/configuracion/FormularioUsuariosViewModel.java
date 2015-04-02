package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

import com.prueba.web.model.Persona;
import com.prueba.web.model.Usuario;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.mvvm.ModeloCombo;
import com.prueba.web.configuracion.service.ServicioControlUsuario;

public class FormularioUsuariosViewModel extends AbstractViewModel implements  EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("servicioControlUsuario")
	private ServicioControlUsuario servicioControlUsuario;
	
	//GUI
	@Wire("#gridPersonas")
	private Listbox gridPersonas;
	
	@Wire("#pagPersonas")
	private Paging pagPersonas;
	
	private Textbox txtUsername;
	
	//Modelos
	private List<Persona> personasSinUsuario;
	private Persona personaSeleccionada;
	private Usuario usuario;
	private Persona personaFiltro;
	
	//Atributos
	private static final int PAGE_SIZE = 5;
	private List<ModeloCombo<Integer>> tiposUsuario;
	private ModeloCombo<Integer> tipoSeleccionado;
	private AbstractValidator validadorUsername;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		pagPersonas.setPageSize(PAGE_SIZE);
		usuario = new Usuario();
		personaFiltro = new Persona() {
		};
		personaSeleccionada = new Persona() {
		};
		
		tiposUsuario = new ArrayList<ModeloCombo<Integer>>();
		tiposUsuario.add(new ModeloCombo<Integer>("Empleados", 1));
		tiposUsuario.add(new ModeloCombo<Integer>("Clientes", 2));
		agregarGridSort(gridPersonas);
		
		validadorUsername = new AbstractValidator() {
			
			@Override
			public void validate(ValidationContext ctx) {
				// TODO Auto-generated method stub
				String username = (String) ctx.getProperty().getValue();
				txtUsername = (Textbox) ctx.getBindContext().getValidatorArg("txtUsername");
				if(username!=null){
					if(servicioControlUsuario.verificarUsername(username)){
						String mensaje = "El Username "+username+" ya esta en uso!";
						mostrarNotification(mensaje, "error", 5000, true, txtUsername);
						addInvalidMessage(ctx, mensaje);
					}
				}
				else{
					String mensaje = "No se permiten campos en blanco.";
					mostrarNotification(mensaje, "error", 5000, true, txtUsername);
					addInvalidMessage(ctx, mensaje);
				}
			}
		};
	}
	
	@Override
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub
		Component component = event.getTarget();
		if(component instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  ((Listheader) component).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarPersonas", parametros );
		}
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange({"personasSinUsuario", "usuario"})
	public void cambiarPersonas(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		if(tipoSeleccionado!=null){
			usuario = new Usuario();
			Map<String, Object> parametros;
			switch (tipoSeleccionado.getValor()) {
			case 1:
				parametros = (Map<String, Object>) servicioControlUsuario.consultarEmpleadosSinUsuarios(
						personaFiltro, fieldSort, sortDirection, page, PAGE_SIZE);
				personasSinUsuario = (List<Persona>) parametros.get("empleados");
				break;
			case 2:
				parametros = (Map<String, Object>) servicioControlUsuario.consultarClientesSinUsuarios(
						personaFiltro, fieldSort, sortDirection, page, PAGE_SIZE);
				personasSinUsuario = (List<Persona>) parametros.get("clientes");
				break;
			default: return;
			}

			Integer total = (Integer) parametros.get("total");
			pagPersonas.setActivePage(page);
			pagPersonas.setTotalSize(total);
		}
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("personasSinUsuario")
	public void consultarPersonas(){
		cambiarPersonas(0, null, null);
	}
	
	@Command
	@NotifyChange("*")
	public void aplicarFiltro(){
		cambiarPersonas(0, null, null);
	}
	
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagPersonas.getActivePage();
		cambiarPersonas(page, null, null);
	}
	
	@Command
	@NotifyChange("personaSeleccionada")
	public void verInfoPersona(@BindingParam("persona") Persona persona){
		personaSeleccionada = persona;
	}
	
	@Command
	@NotifyChange({"personasSinUsuario", "usuario", "personaSeleccionada"})
	public void guardar(@BindingParam("txtPassword") Textbox txtPassword){
		if(personaSeleccionada.getCedula()!=null && !txtPassword.getValue().toString().equalsIgnoreCase("")){
			usuario.setActivo(true);
			usuario.setPersona(personaSeleccionada);
			servicioControlUsuario.grabarUsuario(usuario);
			mostrarMensaje("Informacion", "Usuario Creado Exitosamente", null, null, null, null);
			personaSeleccionada = new Persona() {
			};
			int page = pagPersonas.getActivePage();
			cambiarPersonas(page, null, null);
			txtUsername.setValue(null);
		}
		else if(txtPassword.getValue().toString().equalsIgnoreCase(""))
			mostrarNotification("No se permiten campos en blanco.", "error", 5000, true, txtPassword);
		else
			mostrarMensaje("Error", "No se ha seleccionado ninguna persona", Messagebox.ERROR , null, null, null);
	}
	
	@Command
	public void salir(){
		
		ejecutarGlobalCommand("cambiarUsuarios", null);
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fieldSort",  null);
		parametros.put("sortDirection", null);
		ejecutarGlobalCommand("cambiarUsuariosNoGrupo", parametros);
	}

	/**SETTERS Y GETTERS*/
	public ServicioControlUsuario getServicioControlUsuario() {
		return servicioControlUsuario;
	}

	public void setServicioControlUsuario(
			ServicioControlUsuario servicioControlUsuario) {
		this.servicioControlUsuario = servicioControlUsuario;
	}
	
	public List<Persona> getPersonasSinUsuario() {
		return personasSinUsuario;
	}

	public void setPersonasSinUsuario(List<Persona> personasSinUsuario) {
		this.personasSinUsuario = personasSinUsuario;
	}

	public Persona getPersonaSeleccionada() {
		return personaSeleccionada;
	}
	
	public void setPersonaSeleccionada(Persona personaSeleccionada) {
		this.personaSeleccionada = personaSeleccionada;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Persona getPersonaFiltro() {
		return personaFiltro;
	}

	public void setPersonaFiltro(Persona personaFiltro) {
		this.personaFiltro = personaFiltro;
	}

	public ModeloCombo<Integer> getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(ModeloCombo<Integer> tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}

	public List<ModeloCombo<Integer>> getTiposUsuario() {
		return tiposUsuario;
	}

	public void setTiposUsuario(List<ModeloCombo<Integer>> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}

	public AbstractValidator getValidadorUsername() {
		return validadorUsername;
	}

	public void setValidadorUsername(AbstractValidator validadorUsername) {
		this.validadorUsername = validadorUsername;
	}
}
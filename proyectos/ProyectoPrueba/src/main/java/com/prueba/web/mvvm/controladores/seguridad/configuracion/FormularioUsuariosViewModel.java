package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;

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
	public Listbox gridPersonas;
	
	@Wire("#pagPersonas")
	public Paging pagPersonas;
	
	//Modelos
	private List<Persona> personasSinUsuario;
	private Persona personaSeleccionada;
	private Usuario usuario;
	
	//Atributos
	private List<ModeloCombo<Integer>> tiposUsuario;
	private ModeloCombo<Integer> tipoSeleccionado;
	private static final int PAGE_SIZE = 3;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		pagPersonas.setPageSize(PAGE_SIZE);
		usuario = new Usuario();
		tiposUsuario = new ArrayList<ModeloCombo<Integer>>();
		tiposUsuario.add(new ModeloCombo<Integer>("Empleados", 1));
		tiposUsuario.add(new ModeloCombo<Integer>("Clientes", 2));
		agregarGridSort(gridPersonas);
	}
	
	@Override
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  event.getTarget().getId().toString());
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
		usuario = new Usuario();
		Map<String, Object> parametros;
		switch (tipoSeleccionado.getValor()) {
		case 1:
			parametros = (Map<String, Object>) servicioControlUsuario.consultarEmpleadosSinUsuarios(page, PAGE_SIZE);
			personasSinUsuario = (List<Persona>) parametros.get("empleados");
			break;
		case 2:
			parametros = (Map<String, Object>) servicioControlUsuario.consultarClientesSinUsuarios(page, PAGE_SIZE);
			personasSinUsuario = (List<Persona>) parametros.get("clientes");
			break;
		default: return;
		}
		
		Long total = (Long) parametros.get("total");
		pagPersonas.setActivePage(page);
		pagPersonas.setTotalSize(total.intValue());
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("personasSinUsuario")
	public void consultarPersonas(){
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
	public void guardar(){ //Validar
		usuario.setActivo(true);
		usuario.asignarUsuario(personaSeleccionada);
		servicioControlUsuario.grabarUsuario(usuario);
		mostrarMensaje("Informacion", "Usuario Creado Exitosamente", null, null, null, null);
		personaSeleccionada = null;
		int page = pagPersonas.getActivePage();
		cambiarPersonas(page, null, null);
	}
	
	@Command
	public void salir(){
		ejecutarGlobalCommand("cambiarUsuarios", null);
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setPersonaSeleccionada(Persona personaSeleccionada) {
		this.personaSeleccionada = personaSeleccionada;
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

}

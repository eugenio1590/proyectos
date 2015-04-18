package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.prueba.web.model.GroupMenu;
import com.prueba.web.model.Operacion;
import com.prueba.web.mvvm.AbstractViewModel;

public class ListaOperacionViewModel extends AbstractViewModel {
	
	//GUI
	@Wire("#winOperaciones")
	private Window winOperaciones;
	
	@Wire("#gridOperaciones")
	private Listbox gridOperaciones;
	
	//Modelos
	private List<Operacion> operacionesSeleccionadas;
	private List<Operacion> operaciones;
	private GroupMenu groupMenu;
	
	//Atributos
	private Boolean editar;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("grupoMenu") GroupMenu groupMenu,
			@ExecutionArgParam("editar") Boolean editar){
		super.doAfterCompose(view);
		this.editar = editar;
		this.groupMenu = groupMenu;
		
		if(editar)
			operaciones = this.groupMenu.getMenu().getOperacions();
		else {
			operaciones = this.groupMenu.getOperacions();
			Operacion.ordenarListaOperacion(operaciones);
			gridOperaciones.setCheckmark(false);
		}
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("groupMenu")
	public void guardar(){
		if(operacionesSeleccionadas!=null)
			if(operacionesSeleccionadas.size()>0){
				groupMenu.setOperacions(operacionesSeleccionadas);
				winOperaciones.detach();
			}
			else
				mostrarMensaje("Error", "Debe seleccionar al menos una operacion", Messagebox.ERROR, null, null, null);
		else
			mostrarMensaje("Error", "Debe seleccionar al menos una operacion", Messagebox.ERROR, null, null, null);
	}
	
	/**SETTERS Y GETTERS*/
	public List<Operacion> getOperacionesSeleccionadas() {
		return operacionesSeleccionadas;
	}

	public void setOperacionesSeleccionadas(List<Operacion> operacionesSeleccionadas) {
		this.operacionesSeleccionadas = operacionesSeleccionadas;
	}

	public List<Operacion> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(List<Operacion> operaciones) {
		this.operaciones = operaciones;
	}

	public GroupMenu getGroupMenu() {
		return groupMenu;
	}

	public void setGroupMenu(GroupMenu groupMenu) {
		this.groupMenu = groupMenu;
	}

	public Boolean getEditar() {
		return editar;
	}

	public void setEditar(Boolean editar) {
		this.editar = editar;
	}
}

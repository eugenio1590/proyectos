package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import com.prueba.web.model.Group;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.seguridad.configuracion.service.ServicioControlGrupo;

public class FormularioGruposViewModel extends AbstractViewModel {
	
	//Servicios
	@BeanInjector("servicioControlGrupo")
	private ServicioControlGrupo servicioControlGrupo;
	
	//GUI
	@Wire("#winGrupo")
	private Window winGrupo;
	
	//Modelos
	private Group grupo;
	
	//Atributos
	private String titulo;
	private String operacion;
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("operacion") String operacion,
			@ExecutionArgParam("grupo") Group grupo){
		super.doAfterCompose(view);
		
		titulo = (grupo==null) ? "Registrar Grupo" : "Actualizar Grupo";
		this.grupo = (grupo==null) ? new Group() : grupo;
		this.operacion = operacion;
	}
	
	/**COMMAND*/
	@Command
	public void guardar(){
		if(operacion.equalsIgnoreCase("Insertar") || operacion.equalsIgnoreCase("Modificar"))
			servicioControlGrupo.registrarOActualizarGrupo(grupo);
		ejecutarGlobalCommand("cambiarGrupos", null);
		winGrupo.detach();
		
	}

	/**SETTERS Y GETTERS*/
	public ServicioControlGrupo getServicioControlGrupo() {
		return servicioControlGrupo;
	}

	public void setServicioControlGrupo(ServicioControlGrupo servicioControlGrupo) {
		this.servicioControlGrupo = servicioControlGrupo;
	}
	
	public Group getGrupo() {
		return grupo;
	}

	public void setGrupo(Group grupo) {
		this.grupo = grupo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
}

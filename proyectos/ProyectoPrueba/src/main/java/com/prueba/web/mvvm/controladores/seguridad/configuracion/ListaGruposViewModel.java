package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;

import com.prueba.web.model.Group;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.seguridad.configuracion.service.ServicioControlGrupo;

public class ListaGruposViewModel extends AbstractViewModel implements EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("servicioControlGrupo")
	private ServicioControlGrupo servicioControlGrupo;
	
	//GUI
	@Wire("#gridGrupos")
	private Listbox gridGrupos;
	
	@Wire("#pagGrupos")
	private Paging pagGrupos;
	
	//Modelos
	private List<Group> grupos;
	private Set<Group> gruposSeleccionados;
	private Group grupoFiltro;
	
	//Atributos
	private static final int PAGE_SIZE = 3;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		grupoFiltro = new Group();
		pagGrupos.setPageSize(PAGE_SIZE);
		agregarGridSort(gridGrupos);
		cambiarGrupos(0, null, null);
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("grupos")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub		
		if(event.getTarget() instanceof Listheader){
			//System.out.println(event.getTarget().getParent().getParent().equals(gridGrupos));
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  event.getTarget().getId().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarGrupos", parametros );
		}
		
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("grupos")
	public void cambiarGrupos(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		
		Map<String, Object> parametros = servicioControlGrupo.consultarGrupos(grupoFiltro, fieldSort, sortDirection, page, PAGE_SIZE);
		Long total = (Long) parametros.get("total");
		grupos = (List<Group>) parametros.get("grupos");
		gridGrupos.setMultiple(true);
		gridGrupos.setCheckmark(true);
		pagGrupos.setActivePage(page);
		pagGrupos.setTotalSize(total.intValue());
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagGrupos.getActivePage();
		cambiarGrupos(page, null, null);
	}
	
	@Command
	@NotifyChange("grupos")
	public void aplicarFiltro(){
		cambiarGrupos(0, null, null);
	}
	
	@Command
	public void nuevoGrupo(){
		llamarFormulario("Insertar", null);
	}
	
	@Command
	public void editarGrupo(@BindingParam("grupo") Group grupo){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("grupo", grupo);
		llamarFormulario("Modificar", parametros );
	}
	
	@Command
	@NotifyChange("grupos")
	public void eliminarGrupo(@BindingParam("grupo") Group grupo){
		if(grupo!=null){
			if(servicioControlGrupo.eliminarGrupo(grupo)){
				mostrarMensaje("Informacion", "Grupo Eliminado Exitosamente", null, null, null, null);
				paginarLista();
			}
			else
				mostrarMensaje("Informacion", "El Grupo no se ha podido Eliminar", Messagebox.ERROR, null, null, null);
		
		}
		else if(gruposSeleccionados!=null){
			for(Group grupoSeleccionado : gruposSeleccionados)
				servicioControlGrupo.eliminarGrupo(grupoSeleccionado);
			cambiarGrupos(0, null, null);
			mostrarMensaje("Informacion", "Grupos Eliminados Exitosamente", null, null, null, null);
		}
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	private void llamarFormulario(String operacion, Map<String, Object> parametros){
		parametros=(parametros == null) ? new HashMap<String, Object>() : parametros;
		parametros.put("operacion", operacion);
		crearModal("/WEB-INF/views/sistema/seguridad/configuracion/grupos/formularioGrupos.zul", parametros);
	}

	/**SETTERS Y GETTERS*/
	public ServicioControlGrupo getServicioControlGrupo() {
		return servicioControlGrupo;
	}

	public void setServicioControlGrupo(ServicioControlGrupo servicioControlGrupo) {
		this.servicioControlGrupo = servicioControlGrupo;
	}
	
	public List<Group> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Group> grupos) {
		this.grupos = grupos;
	}

	public Set<Group> getGruposSeleccionados() {
		return gruposSeleccionados;
	}

	public void setGruposSeleccionados(Set<Group> gruposSeleccionados) {
		this.gruposSeleccionados = gruposSeleccionados;
	}

	public Group getGrupoFiltro() {
		return grupoFiltro;
	}

	public void setGrupoFiltro(Group grupoFiltro) {
		this.grupoFiltro = grupoFiltro;
	}
	
}
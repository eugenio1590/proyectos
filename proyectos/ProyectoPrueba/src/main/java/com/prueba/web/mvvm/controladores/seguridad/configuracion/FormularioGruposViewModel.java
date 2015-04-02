package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import java.util.ArrayList;
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
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.prueba.web.configuracion.service.ServicioControlUsuario;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.Persona;
import com.prueba.web.model.Usuario;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.seguridad.configuracion.service.ServicioControlGrupo;

public class FormularioGruposViewModel extends AbstractViewModel implements EventListener<SortEvent> {
	
	//Servicios
	@BeanInjector("servicioControlGrupo")
	private ServicioControlGrupo servicioControlGrupo;
	
	@BeanInjector("servicioControlUsuario")
	private ServicioControlUsuario servicioControlUsuario;
	
	//GUI
	@Wire("#winGrupo")
	private Window winGrupo;
	
	@Wire("#gridMiembrosGrupo")
	private Listbox gridMiembrosGrupo;
	
	@Wire("#pagMiembrosGrupo")
	private Paging pagMiembrosGrupo;
	
	@Wire("#gridUsuariosNoGrupo")
	private Listbox gridUsuariosNoGrupo;
	
	@Wire("#pagUsuariosNoGrupo")
	private Paging pagUsuariosNoGrupo;
	
	//Modelos
	private Group grupo;
	
	private List<GroupMember> usuariosGrupo;
	private Set<GroupMember> miembrosSeleccionados;
	private Persona miembroFiltro;
	
	private List<Usuario> usuariosNoGrupo;
	private Set<Usuario> usuariosSeleccionados;
	private Persona usuarioFiltro;
	
	//Atributos
	private static final int PAGE_SIZE = 8;
	private String titulo;
	private String operacion;
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("operacion") String operacion,
			@ExecutionArgParam("grupo") Group grupo){
		super.doAfterCompose(view);
		this.operacion = operacion;
		usuarioFiltro = new Persona() {
		};
		miembroFiltro = new Persona() {
		};
		
		pagUsuariosNoGrupo.setPageSize(PAGE_SIZE);
		agregarGridSort(gridUsuariosNoGrupo);
		pagMiembrosGrupo.setPageSize(PAGE_SIZE);
		agregarGridSort(gridMiembrosGrupo);
		
		if(grupo==null){
			titulo = "Registrar Grupo";
			this.grupo = new Group();
			usuariosGrupo = new ArrayList<GroupMember>();
			this.grupo.setGroupMembers(usuariosGrupo);
			//Faltan los Usuario del No Grupo
			cambiarUsuariosNoGrupo(0, null, null);
		}
		else{
			titulo = "Actualizar Grupo";
			this.grupo = grupo;
			cambiarUsuariosGrupo(0, null, null);
			cambiarUsuariosNoGrupo(0, null, null);
		}
		
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("usuarios")
	public void onEvent(SortEvent event) throws Exception {
		Component component = event.getTarget();
		System.out.println("PASO EVENTO");
		if(component instanceof Listheader){
			Listbox gridParent = (Listbox) component.getParent().getParent();
			if(gridParent.equals(gridMiembrosGrupo)){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("fieldSort",  ((Listheader) component).getValue().toString());
				parametros.put("sortDirection", event.isAscending());
				ejecutarGlobalCommand("cambiarUsuariosGrupo", parametros );
			}
			else if(gridParent.equals(gridUsuariosNoGrupo)){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("fieldSort",  ((Listheader) component).getValue().toString());
				parametros.put("sortDirection", event.isAscending());
				ejecutarGlobalCommand("cambiarUsuariosNoGrupo", parametros);
			}
		}
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("usuariosNoGrupo")
	public void cambiarUsuariosNoGrupo(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		int idGrupo = (this.grupo.getId()==null) ? -1 : this.grupo.getId();
		Map<String, Object> parametros = servicioControlUsuario.consultarUsuariosNoAsignadosGrupo(usuarioFiltro, idGrupo, fieldSort, sortDirection, page, PAGE_SIZE);
		Integer total = (Integer) parametros.get("total");
		usuariosNoGrupo = (List<Usuario>) parametros.get("usuarios");
		gridUsuariosNoGrupo.setMultiple(true);
		gridUsuariosNoGrupo.setCheckmark(true);
		pagUsuariosNoGrupo.setActivePage(page);
		pagUsuariosNoGrupo.setTotalSize(total);
	}
	
	@GlobalCommand
	@NotifyChange("usuariosGrupo")
	public void cambiarUsuariosGrupo(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		int idGrupo = (this.grupo.getId()==null) ? -1 : this.grupo.getId();
		Map<String, Object> parametros = servicioControlGrupo.consultarMiembrosGrupo(miembroFiltro, idGrupo, fieldSort, sortDirection, page, PAGE_SIZE); 
		Integer total = (Integer) parametros.get("total");
		usuariosGrupo = (List<GroupMember>) parametros.get("miembros");
		gridMiembrosGrupo.setMultiple(true);
		gridMiembrosGrupo.setCheckmark(true);
		pagMiembrosGrupo.setActivePage(page);
		pagMiembrosGrupo.setTotalSize(total);
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange({"usuariosNoGrupo", "usuariosGrupo"})
	public void aplicarFiltro(@BindingParam("tipo") int tipo){
		switch(tipo){
		case 1: cambiarUsuariosNoGrupo(0, null, null);
			break;
		case 2: cambiarUsuariosGrupo(0, null, null);
			break;
		default: break;
		}
	}
	
	@Command
	public void nuevoUsuario(){
		this.crearModal("/WEB-INF/views/sistema/seguridad/configuracion/usuarios/formularioUsuarios.zul", null);
	}
	
	@Command
	@NotifyChange({"usuariosGrupo", "usuariosNoGrupo"})
	public void agregarMiembros(){
		if(usuariosSeleccionados!=null)
			if(usuariosSeleccionados.size()>0){
				for(Usuario usuario : usuariosSeleccionados)
				{
					GroupMember miembro = new GroupMember();
					miembro.setUsuario(usuario);
					miembro.setGroup(grupo);
					usuariosGrupo.add(miembro);
				}
				usuariosNoGrupo.removeAll(usuariosSeleccionados);
				usuariosSeleccionados.clear();
			}
	}
	
	@Command
	@NotifyChange({"usuariosGrupo", "usuariosNoGrupo"})
	public void removerMiembros(){
		if(miembrosSeleccionados!=null)
			if(miembrosSeleccionados.size()>0){
				for(GroupMember miembro : miembrosSeleccionados){
					Usuario usuario = miembro.getUsuario();
					usuariosNoGrupo.add(usuario);
				}
				usuariosGrupo.removeAll(miembrosSeleccionados);
				miembrosSeleccionados.clear();
			}
	}
	
	@Command
	public void guardar(){
		if(operacion.equalsIgnoreCase("Insertar") || operacion.equalsIgnoreCase("Modificar")){
			for(GroupMember miembro : usuariosGrupo)
				System.out.println(miembro.getUsuario().getUsername());
			grupo.setGroupMembers(usuariosGrupo);
			servicioControlGrupo.registrarOActualizarGrupo(grupo);
		}
		ejecutarGlobalCommand("cambiarGrupos", null);
		winGrupo.detach();
		
	}

	/**SETTERS Y GETTERS*/
	
	public ServicioControlUsuario getServicioControlUsuario() {
		return servicioControlUsuario;
	}

	public void setServicioControlUsuario(
			ServicioControlUsuario servicioControlUsuario) {
		this.servicioControlUsuario = servicioControlUsuario;
	}
	
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

	public List<Usuario> getUsuariosNoGrupo() {
		return usuariosNoGrupo;
	}

	public void setUsuariosNoGrupo(List<Usuario> usuariosNoGrupo) {
		this.usuariosNoGrupo = usuariosNoGrupo;
	}

	public Set<Usuario> getUsuariosSeleccionados() {
		return usuariosSeleccionados;
	}

	public void setUsuariosSeleccionados(Set<Usuario> usuariosSeleccionados) {
		this.usuariosSeleccionados = usuariosSeleccionados;
	}

	public List<GroupMember> getUsuariosGrupo() {
		return usuariosGrupo;
	}

	public void setUsuariosGrupo(List<GroupMember> usuariosGrupo) {
		this.usuariosGrupo = usuariosGrupo;
	}

	public Set<GroupMember> getMiembrosSeleccionados() {
		return miembrosSeleccionados;
	}

	public void setMiembrosSeleccionados(Set<GroupMember> miembrosSeleccionados) {
		this.miembrosSeleccionados = miembrosSeleccionados;
	}

	public String getTitulo() {
		return titulo;
	}

	public Persona getUsuarioFiltro() {
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(Persona usuarioFiltro) {
		this.usuarioFiltro = usuarioFiltro;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Persona getMiembroFiltro() {
		return miembroFiltro;
	}

	public void setMiembroFiltro(Persona miembroFiltro) {
		this.miembroFiltro = miembroFiltro;
	}
}

package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

import com.prueba.web.model.Usuario;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.configuracion.service.ServicioControlUsuario;

public class ListaUsuariosViewModel extends AbstractViewModel implements EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("servicioControlUsuario")
	private ServicioControlUsuario servicioControlUsuario;
	
	//GUI
	@Wire("#gridUsuarios")
	private Listbox gridUsuarios;
	
	@Wire("#pagUsuarios")
	private Paging pagUsuarios;
	
	@Wire("#radEstado")
	private Radiogroup radEstado;
	
	//Modelos
	private List<Usuario> usuarios;
	private Set<Usuario> usuariosSeleccionados;
	private Usuario usuarioFiltro;
	
	//Atributos
	private static final int PAGE_SIZE = 3;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		usuarioFiltro = new Usuario();
		pagUsuarios.setPageSize(PAGE_SIZE);
		agregarGridSort(gridUsuarios);
		cambiarUsuarios(0, null, null);
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("usuarios")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub	
		Component component = event.getTarget();
		if(component instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  ((Listheader) component).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarUsuarios", parametros );
		}
		
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("usuarios")
	public void cambiarUsuarios(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		Map<String, Object> parametros = servicioControlUsuario.consultarUsuarios(
				usuarioFiltro, fieldSort, sortDirection, page, PAGE_SIZE);
		Integer total = (Integer) parametros.get("total");
		usuarios = (List<Usuario>) parametros.get("usuarios");
		gridUsuarios.setMultiple(true);
		gridUsuarios.setCheckmark(true);
		pagUsuarios.setActivePage(page);
		pagUsuarios.setTotalSize(total);
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagUsuarios.getActivePage();
		cambiarUsuarios(page, null, null);
	}
	
	@Command
	@NotifyChange("usuarios")
	public void aplicarFiltro(){
		Radio selectedItem = radEstado.getSelectedItem();
		this.usuarioFiltro.setActivo((selectedItem!=null) ? Boolean.valueOf((String)selectedItem.getValue()) : null);
		cambiarUsuarios(0, null, null);
	}
	
	@Command
	@NotifyChange("usuarios")
	public void limpiarRadios(){
		this.usuarioFiltro.setActivo(null);
		radEstado.setSelectedIndex(-1);
		aplicarFiltro();
	}
	
	@Command
	public void nuevoUsuario(){
		llamarFormulario("formularioUsuarios.zul", null);
	}
	
	@Command
	public void editarUsuario(@BindingParam("usuario") Usuario usuario){
		Usuario userSession = consultarUsuarioSession();
		if(userSession.getId()!=usuario.getId()){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", usuario);
			llamarFormulario("editarUsuario.zul", parametros);
		}
		else
			mostrarMensaje("Error", "No se puede Editar el Usuario de la Session", Messagebox.ERROR, null, null, null);
	}
	
	@Command
	@NotifyChange("usuarios")
	public void actualizarEstado(@BindingParam("usuario") Usuario usuario, @BindingParam("estado") Boolean estado){
		Usuario userSession = consultarUsuarioSession();
		if(userSession.getId()!=usuario.getId()){
			if(servicioControlUsuario.cambiarEstadoUsuario(usuario, estado)){
				String mensaje = (estado) ? "Activado" : "Desactivado";
				mostrarMensaje("Informacion", "Usuario "+mensaje+" Exitosamente", null, null, null, null);
				paginarLista();
			}
		}
		else
			mostrarMensaje("Error", "No se puede Desactivar el Usuario de la Session", Messagebox.ERROR, null, null, null);
	}
	
	@Command
	@NotifyChange("usuarios")
	public void eliminarUsuario(@BindingParam("usuario") Usuario usuario){
		Usuario userSession = consultarUsuarioSession();
		if(usuario!=null){
			if(userSession.getId()!=usuario.getId())
				if(servicioControlUsuario.eliminarUsuario(usuario)){
					mostrarMensaje("Informacion", "Usuario Eliminado Exitosamente", null, null, null, null);
					paginarLista();
				}
				else
					mostrarMensaje("Informacion", "El Usuario no se ha podido Eliminar", Messagebox.ERROR, null, null, null);
			else
				mostrarMensaje("Error", "No se puede Eliminar el Usuario de la Session", Messagebox.ERROR, null, null, null);
		
		}
		else if(usuariosSeleccionados!=null){
			for(Usuario usuarioSeleccionado : usuariosSeleccionados)
				if(userSession.getId()!=usuarioSeleccionado.getId())
					servicioControlUsuario.eliminarUsuario(usuarioSeleccionado);
			cambiarUsuarios(0, null, null);
			mostrarMensaje("Informacion", "Usuarios Eliminados Exitosamente", null, null, null, null);
		}
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	private Usuario consultarUsuarioSession(){
		UserDetails user = this.getUser();
		return servicioControlUsuario.consultarUsuario(user.getUsername(), user.getPassword());
	}
	
	private void llamarFormulario(String ruta, Map<String, Object> parametros){
		crearModal("/WEB-INF/views/sistema/seguridad/configuracion/usuarios/"+ruta, parametros);
	}

	/**SETTERS Y GETTERS*/
	public ServicioControlUsuario getServicioControlUsuario() {
		return servicioControlUsuario;
	}

	public void setServicioControlUsuario(
			ServicioControlUsuario servicioControlUsuario) {
		this.servicioControlUsuario = servicioControlUsuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Set<Usuario> getUsuariosSeleccionados() {
		return usuariosSeleccionados;
	}

	public void setUsuariosSeleccionados(Set<Usuario> usuariosSeleccionados) {
		this.usuariosSeleccionados = usuariosSeleccionados;
	}
	
	public Usuario getUsuarioFiltro() {
		return usuarioFiltro;
	}
}

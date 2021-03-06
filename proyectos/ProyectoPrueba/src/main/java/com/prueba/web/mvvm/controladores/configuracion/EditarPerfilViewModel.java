package com.prueba.web.mvvm.controladores.configuracion;

import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;

import com.prueba.web.model.Usuario;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.configuracion.service.ServicioControlUsuario;

public class EditarPerfilViewModel extends AbstractViewModel implements EventListener<UploadEvent> {
	
	//Servicios
	@BeanInjector("servicioControlUsuario")
	private ServicioControlUsuario servicioControlUsuario;
	
	//GUI
	@Wire("#imgFoto")
	private Image imgFoto;
	
	@Wire("#btnCambFoto")
	private Button btnCambFoto;
	
	@Wire("#txtClaveNueva")
	private Textbox txtClaveNueva;
	
	@Wire("#txtClaveNuevaConf")
	private Textbox txtClaveNuevaConf;
	
	//Modelos
	private Usuario usuario;
	
	//Atributos
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		
		btnCambFoto.addEventListener("onUpload", this);
		
		UserDetails user = this.getUser();
		if(user!=null)
			usuario = this.servicioControlUsuario.consultarUsuario(user.getUsername(), user.getPassword());
	}

	/**INTERFACES*/
	//1. EventListener<UploadEvent>
	@Override
	public void onEvent(UploadEvent event) throws Exception {
		// TODO Auto-generated method stub
		Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image)
			imgFoto.setContent((org.zkoss.image.Image) media);
		else if (media != null)
			mostrarMensaje("Error", "No es una imagen: " + media, null, null, null, null);
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("usuario")
	public void enviar(){
		String nuevaClave = txtClaveNueva.getValue();
		String nuevaClaveConf = txtClaveNuevaConf.getValue();
		org.zkoss.image.Image foto = this.imgFoto.getContent();
		
		if(foto!=null)
			usuario.setFoto(foto.getByteData());
		
		if(!(nuevaClave.equalsIgnoreCase("") && nuevaClaveConf.equalsIgnoreCase(""))){ //Arreglar
			if(nuevaClave.equalsIgnoreCase(nuevaClaveConf)){
				usuario.setPasword(nuevaClave);
				usuario=servicioControlUsuario.actualizarUsuario(usuario, true);
			}
			else
				mostrarMensaje("Error", "Las Contraseņas no son iguales", null, null, null, null);
		}
		else
			usuario=servicioControlUsuario.actualizarUsuario(usuario, false);
		
		mostrarMensaje("Informacion", "Datos Guardados Satisfactoriamente", null, null, null, null);
		txtClaveNueva.setValue("");
		txtClaveNuevaConf.setValue("");
	}
	
	/**SETTERS Y GETTERS*/
	public ServicioControlUsuario getServicioControlUsuario() {
		return servicioControlUsuario;
	}

	public void setServicioControlUsuario(
			ServicioControlUsuario servicioControlUsuario) {
		this.servicioControlUsuario = servicioControlUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}

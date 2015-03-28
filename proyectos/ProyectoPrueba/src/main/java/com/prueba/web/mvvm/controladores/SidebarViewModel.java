package com.prueba.web.mvvm.controladores;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Span;

import com.prueba.web.model.Menu;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.mvvm.ModelNavbar;
import com.prueba.web.service.configuracion.ServicioControlUsuario;

public class SidebarViewModel extends AbstractViewModel implements SerializableEventListener{
	
	private static final long serialVersionUID = -1838333481895117449L;
	
	//Servicios
	@BeanInjector("servicioControlUsuario")
	private ServicioControlUsuario servicioControlUsuario;
	
	//GUI
	@Wire("#navbar")
	private Navbar navbar;
	
	private Div hrefEnlacesMenu;

	public SidebarViewModel() {
		// TODO Auto-generated constructor stub
	}
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		
		List<ModelNavbar> modelo = new ArrayList<ModelNavbar>();
		
		for(Menu menu : servicioControlUsuario.consultarMenus())
			modelo.add(menu);
		
		constructMenu(modelo, this.navbar);
	}

	/**INTERFACES*/
	//1. SerializableEventListener
	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub
		Navitem item = navbar.getSelectedItem();
		
		insertComponent(navbar.getPage(), "#mainInclude", item.getAttribute("locationUri").toString());
        
        if(hrefEnlacesMenu==null)
        	hrefEnlacesMenu = (Div) findComponent(navbar.getPage(), "#hrefEnlacesMenu");
        
        crearEncabezadoMenu(item);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/*
	 * Descripcion: Permitira crear el Encabezado del Menu de acuerdo al item Seleccionado
	 * @param item. item del menu seleccionado
	 * Retorno: Ninguno
	 * */
	protected void crearEncabezadoMenu(Navitem item){
		//Borramos los Hijos
        hrefEnlacesMenu.getChildren().clear();
        
        //Nuevo Enlace del Menu
        Div nuevoEnlace = new Div();
        nuevoEnlace.setSclass("breadcrumb");
        
        //Inicializacion de las Variables del Ciclo
        boolean wPadre = true;
        Component component = item;
        List<String> titulos = new ArrayList<String>();
        
        while(wPadre){
        	if(!(component instanceof Navbar)){
        		String titulo;
        		
        		if(component instanceof Nav)
        			titulo=((Nav) component).getLabel();
        		else
        			titulo=((Navitem) component).getLabel();
        		
        		titulo=(component.getParent() instanceof Navbar) 
        				? titulo : " > "+titulo;
        		
        		titulos.add(titulo);
            	component=component.getParent();
        	}
        	else
        		wPadre = false;
        }
        
        for(int i=titulos.size()-1; i>=0; i--){
        	component = new Label();
        	((Label) component).setValue(titulos.get(i));
        		
        	if(i==titulos.size()-1)
        	{
        		Span icono = new Span();
        		icono.setSclass("home-icon z-icon-home");
        		nuevoEnlace.appendChild(icono);
        		nuevoEnlace.appendChild(component);
        	}
        	else
        		nuevoEnlace.appendChild(component);
        }
        
        hrefEnlacesMenu.appendChild(nuevoEnlace);
	}

	/**SETTERS Y GETTERS*/
	public ServicioControlUsuario getServicioControlUsuario() {
		return servicioControlUsuario;
	}

	public void setServicioControlUsuario(
			ServicioControlUsuario servicioControlUsuario) {
		this.servicioControlUsuario = servicioControlUsuario;
	}
}

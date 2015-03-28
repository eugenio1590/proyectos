package com.prueba.web.mvvm.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;

import com.prueba.web.model.Articulo;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.service.ServiceControlInventario;

public class InicioViewModel extends AbstractViewModel {
	
	//Servicios
	@BeanInjector("serviceControlInventario")
	private ServiceControlInventario serviceControlInventario;
	
	//GUI
	@Wire("#gridArticulos")
	private Listbox gridArticulos;

	@Wire("#btnActualizar")
	private Button btnActualizar;

	@Wire("#pagArticulos")
	private Paging pagArticulos;
	
	@Wire("#gridArticulosCompra")
	private Listbox gridArticulosCompra;
	
	//Modelos
	private List<Articulo> articulos;
	private Set<Articulo> articulosSeleccionados;
	private List<Articulo> articulosCompra;
	private Set<Articulo> articulosCompraSeleccionados;

	//Atributos
	private static final int PAGE_SIZE = 3;
	private String codigoFiltro;
	private String nombreFiltro;
	
	@AfterCompose
	@NotifyChange("*")
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		pagArticulos.setPageSize(PAGE_SIZE);
		articulosCompra = new ArrayList<Articulo>();
		cambiarArticulos(0);
	}
	
	@Command
	public void iniciarSession(){
		this.redireccionar("/inicioSession");
	}
	
	@Command
	@NotifyChange("articulos")
	public void paginarLista(){
		int page=pagArticulos.getActivePage();
		cambiarArticulos(page);
	}
	
	@Command
	@NotifyChange("articulos")
	public void aplicarFiltro(){
		cambiarArticulos(0);
	}
	
	@Command
	@NotifyChange({"articulos", "articulosCompra", "articulosSeleccionados"})
	public void agregarArticulos(){
		moveSelection(articulos, articulosCompra, articulosSeleccionados, "No se Ha Seleccionado Nada");
	}
	
	@Command
	@NotifyChange({"articulosCompra", "articulos", "articulosCompraSeleccionados"})
	public void removerArticulos(){
		moveSelection(articulosCompra, articulos, articulosCompraSeleccionados, "No se Ha Seleccionado Nada");
	}
	
	@Command
	public void grabar(){
		ListModel<Articulo> articulos=gridArticulos.getModel();
		for(int i=0; i<articulos.getSize(); i++){
			Articulo articulo = articulos.getElementAt(i);
			System.out.println(articulo.getCodigo());
			System.out.println(articulo.getCosto());
		}
		System.out.println("Articulos Seleccionados");
		for(Articulo articulo : articulosSeleccionados){
			System.out.println(articulo.getCodigo());
			System.out.println(articulo.getCosto());
		}
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	@NotifyChange("articulos")
	private void cambiarArticulos(int page){
		Map<String, Object> parametros = serviceControlInventario.consultarArticulosCodigoONombre(codigoFiltro, nombreFiltro, page, PAGE_SIZE);
		Long total = (Long) parametros.get("total");
		articulos = (List<Articulo>) parametros.get("articulos");
		gridArticulos.setMultiple(true);
		gridArticulos.setCheckmark(true);
		pagArticulos.setActivePage(page);
		pagArticulos.setTotalSize(total.intValue());
	}

	/**SETTERS Y GETTERS*/
	public ServiceControlInventario getServiceControlInventario() {
		return serviceControlInventario;
	}

	public void setServiceControlInventario(
			ServiceControlInventario serviceControlInventario) {
		this.serviceControlInventario = serviceControlInventario;
	}

	public List<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
	}

	public Set<Articulo> getArticulosSeleccionados() {
		return articulosSeleccionados;
	}

	public void setArticulosSeleccionados(Set<Articulo> articulosSeleccionados) {
		this.articulosSeleccionados = articulosSeleccionados;
	}

	public List<Articulo> getArticulosCompra() {
		return articulosCompra;
	}

	public void setArticulosCompra(List<Articulo> articulosCompra) {
		this.articulosCompra = articulosCompra;
	}

	public Set<Articulo> getArticulosCompraSeleccionados() {
		return articulosCompraSeleccionados;
	}

	public void setArticulosCompraSeleccionados(
			Set<Articulo> articulosCompraSeleccionados) {
		this.articulosCompraSeleccionados = articulosCompraSeleccionados;
	}

	public String getCodigoFiltro() {
		return codigoFiltro;
	}

	public void setCodigoFiltro(String codigoFiltro) {
		this.codigoFiltro = codigoFiltro;
	}

	public String getNombreFiltro() {
		return nombreFiltro;
	}

	public void setNombreFiltro(String nombreFiltro) {
		this.nombreFiltro = nombreFiltro;
	}
}

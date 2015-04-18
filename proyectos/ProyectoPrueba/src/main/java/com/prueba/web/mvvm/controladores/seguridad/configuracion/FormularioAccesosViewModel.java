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
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.prueba.web.configuracion.service.ServicioControlUsuario;
import com.prueba.web.model.Arbol;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMenu;
import com.prueba.web.model.Menu;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.mvvm.ModelTree;
import com.prueba.web.seguridad.configuracion.service.ServicioControlGrupo;

public class FormularioAccesosViewModel extends AbstractViewModel implements EventListener<Messagebox.ClickEvent>{
	
	//Servicios
	@BeanInjector("servicioControlUsuario")
	private ServicioControlUsuario servicioControlUsuario;
	
	@BeanInjector("servicioControlGrupo")
	private ServicioControlGrupo servicioControlGrupo;
	
	//GUI	
	@Wire("#pagMenuNoAsignado")
	private Paging pagMenuNoAsignado;
	
	@Wire("#pagMenuAsignado")
	private Paging pagMenuAsignado;
	
	//Modelos
	private ModelTree<Menu> menuNoAsignadoTree;
	private Set<DefaultTreeNode<Menu>> nodosNoAsignadosTree;
	
	private ModelTree<GroupMenu> menuAsignadoTree;
	private Set<DefaultTreeNode<GroupMenu>> nodosAsignadosTree;
	
	private Group grupo;
	
	//Atributos
	private static final int PAGE_SIZE = 5;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("grupo") Group grupo){
		super.doAfterCompose(view);
		this.grupo = grupo;
		pagMenuNoAsignado.setPageSize(PAGE_SIZE);
		pagMenuAsignado.setPageSize(PAGE_SIZE);
		cambiarMenuNoAsignadoGrupo(0);
		cambiarMenuAsignadoGrupo(0);
	}
	
	/**Interface*/
	//1. EventListener<Messagebox.ClickEvent>
	@Override
	public void onEvent(ClickEvent event) throws Exception {
		// TODO Auto-generated method stub
		if (Messagebox.Button.YES.equals(event.getButton())) {
			guardar(false);
			paginarLista(2, false);
			notificarCambios("menuAsignadoTree");
		}
		else if (Messagebox.Button.NO.equals(event.getButton())){
			paginarLista(2, false);
			notificarCambios("menuAsignadoTree");
		}else if (Messagebox.Button.CANCEL.equals(event.getButton())) {
			int page = pagMenuAsignado.getActivePage();
			pagMenuAsignado.setActivePage(page-1);
		}
	}	
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("menuNoAsignadoTree")
	public void cambiarMenuNoAsignadoGrupo(@Default("0") int page){
		Map<String, Object> parametros = servicioControlUsuario.consultarRootPadres(page, PAGE_SIZE);
		Integer total = (Integer) parametros.get("total");
		List<Menu> menuNoAsignado = (List<Menu>) parametros.get("menu");
		menuNoAsignadoTree = new ModelTree<Menu>(menuNoAsignado.toArray(new Menu[]{}), true, true, true);
		pagMenuNoAsignado.setActivePage(page);
		pagMenuNoAsignado.setTotalSize(total);
	}
	
	@GlobalCommand
	@NotifyChange("menuAsignadoTree")
	public void cambiarMenuAsignadoGrupo(@Default("0") int page){
		Map<String, Object> parametros = servicioControlGrupo.consultarPadresMenuAsignadoGrupo(
				grupo.getId(), page, PAGE_SIZE);
		Integer total = (Integer) parametros.get("total");
		List<GroupMenu> menuAsignado = (List<GroupMenu>) parametros.get("menu");
		menuAsignadoTree = new ModelTree<GroupMenu>(menuAsignado.toArray(new GroupMenu[]{}), true, true, false);
		pagMenuNoAsignado.setActivePage(page);
		pagMenuAsignado.setTotalSize(total);
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange({"menuNoAsignadoTree", "menuAsignadoTree"})
	public void paginarLista(@BindingParam("tipo") int tipo, @Default("true") Boolean withAdvert){
		int page;
		switch (tipo) {
		case 1:
			page = pagMenuNoAsignado.getActivePage();
			cambiarMenuNoAsignadoGrupo(page);
			
			break;
			
		case 2:
			if(withAdvert){
				mostrarMensaje("Advertencia", "Los cambios efectuados anteriormente no se efectuaran. "+
					"Desea guardar antes de cambiar de pagina?", Messagebox.EXCLAMATION, new Messagebox.Button[]{
						Messagebox.Button.YES, Messagebox.Button.NO, Messagebox.Button.CANCEL}, this, null);
			}
			else {
				page = pagMenuAsignado.getActivePage();
				System.out.println("PAGINA "+page);
				cambiarMenuAsignadoGrupo(page);
			}
			
			break;

		default:break;
		}
	}
	
	@Command
	public void openChild(@BindingParam("component") Component component,
			@BindingParam("nodo") DefaultTreeNode<Menu> nodo,
			@Default("false") @BindingParam("wOpen") Boolean wOpen){
		Treeitem treeItem = null;
		if(component instanceof Treecell){
			Treecell treCell = (Treecell) component;
			treeItem = (Treeitem) treCell.getParent().getParent();
		}
		else if(component instanceof Treerow){
			Treerow treerow = (Treerow) component;
			treeItem = (Treeitem) treerow.getParent();
		}
		else if(component instanceof Treeitem)
			treeItem = (Treeitem) component;
		
		Boolean open = wOpen;
		if(wOpen){
			open = treeItem.isOpen();
			treeItem.setOpen((open) ? false : true);
		}
		
		if(!open && nodo!=null)
			openChild(nodo);
	}
	
	@Command
	@NotifyChange({"menuAsignadoTree", "nodosNoAsignadosTree", "menuNoAsignadoTree", "nodosAsignadosTree"})
	public void agregarSeleccionados(@BindingParam("tipo") int tipo){
		switch (tipo) {
		case 1: moveSelectedTree(menuNoAsignadoTree, menuAsignadoTree, nodosNoAsignadosTree); break;
			
		case 2: moveSelectedTree(menuAsignadoTree, menuNoAsignadoTree, nodosAsignadosTree); break;

		default: break;
		}
	}
	
	@Command
	public void mostrarEdicionOperaciones(@BindingParam("nodo") DefaultTreeNode nodo,
			@BindingParam("editar") boolean editar){
		Map<String, Object> parametros = new HashMap<String, Object>();
		GroupMenu grupoMenu = null;
		if(nodo.getData() instanceof GroupMenu)
			grupoMenu = (GroupMenu) nodo.getData();
		else {
			TreeNode<GroupMenu> nodoTemp = this.menuAsignadoTree.findNode((GroupMenu) getInstanceToMove((Arbol) nodo.getData()));
			if(nodoTemp!=null)
				grupoMenu = nodoTemp.getData();
			else {
				return;
			} //Se Mostrara un Mensaje
		}
		
		parametros.put("grupoMenu", grupoMenu);
		parametros.put("editar", editar);
		crearModal("/WEB-INF/views/sistema/seguridad/configuracion/accesos/listaOperaciones.zul", parametros);
	}
	
	@Command
	@NotifyChange("*")
	public void guardar(@Default("false") @BindingParam("wMensaje") Boolean wMensaje){
		int page = pagMenuAsignado.getActivePage(); //Numero de pagina actual
		List<GroupMenu> menuGrupo = menuAsignadoTree.toList();
		if(servicioControlGrupo.actualizarGroupMenu(menuGrupo, this.grupo.getId(), page, PAGE_SIZE) && wMensaje)
			mostrarMensaje("Informacion", "Actualizacion Exitosa del Menu", null, null, null, null);
		else if(wMensaje)
			mostrarMensaje("Error", "Ha Ocurrido un Error al Actualizar el menu del Grupo", Messagebox.ERROR, null, null, null);
	}

	/**METODOS PROPIOS DE LA CLASE*/
	private void openChild(DefaultTreeNode<Menu> nodo){
		Map<String, Object> parametros;
		Integer idNodo = nodo.getData().getId();
		parametros=servicioControlUsuario.consultarSubRamas(idNodo, 0, -1);
		List<Menu> subRamas = (List<Menu>) parametros.get("menu");
		parametros=servicioControlUsuario.consultarHijosNoAsignadoGrupo(grupo.getId(), idNodo, 0, -1);
		List<Menu> hijosNoAsignados = (List<Menu>) parametros.get("menu");
		subRamas.addAll(hijosNoAsignados);
		Arbol.ordenarListaArbol(subRamas);
		Menu[] child=subRamas.toArray(new Menu[]{});
		menuNoAsignadoTree.loadChild(nodo, child);
	}
	
	/**TODAVIA TIENE ERRORES*/
	private <T extends Arbol, Y extends Arbol> void moveSelectedTree(
			ModelTree<T> origen, 
			ModelTree<Y> destino, 
			Set<DefaultTreeNode<T>> seleccion){
		
		if(seleccion!=null){
			for(DefaultTreeNode<T> nodo : seleccion){
				System.out.println("NODO A AGREGAR");
				System.out.println(nodo.getData().getLabel());
				System.out.println("---FIN---");
				
				Y model = getInstanceToMove(nodo.getData());
				destino.addNode(model, false);
				origen.removeNode(nodo, false);
			}
			
			seleccion.clear();
			origen.sort(true);
			destino.sort(true);
		}
	}
	
	private <T extends Arbol> T getInstanceToMove(Arbol data){
		Arbol instancia = null;
		if(data instanceof Menu){
			instancia = new GroupMenu();
			((GroupMenu) instancia).setGroup(grupo);
			((GroupMenu) instancia).setMenu((Menu) data);
		}
		else if(data instanceof GroupMenu){
			instancia = ((GroupMenu) data).getMenu();
			((GroupMenu) data).setMenu(null);
		}
		return (T) instancia;
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

	public ModelTree<Menu> getMenuNoAsignadoTree() {
		return menuNoAsignadoTree;
	}

	public void setMenuNoAsignadoTree(ModelTree<Menu> menuNoAsignadoTree) {
		this.menuNoAsignadoTree = menuNoAsignadoTree;
	}
	
	public ModelTree<GroupMenu> getMenuAsignadoTree() {
		return menuAsignadoTree;
	}

	public void setMenuAsignadoTree(ModelTree<GroupMenu> menuAsignadoTree) {
		this.menuAsignadoTree = menuAsignadoTree;
	}
	
	public Set<DefaultTreeNode<Menu>> getNodosNoAsignadosTree() {
		return nodosNoAsignadosTree;
	}

	public void setNodosNoAsignadosTree(
			Set<DefaultTreeNode<Menu>> nodosNoAsignadosTree) {
		this.nodosNoAsignadosTree = nodosNoAsignadosTree;
	}

	public Set<DefaultTreeNode<GroupMenu>> getNodosAsignadosTree() {
		return nodosAsignadosTree;
	}

	public void setNodosAsignadosTree(
			Set<DefaultTreeNode<GroupMenu>> nodosAsignadosTree) {
		this.nodosAsignadosTree = nodosAsignadosTree;
	}

	public Group getGrupo() {
		return grupo;
	}
	
	public void setGrupo(Group grupo) {
		this.grupo = grupo;
	}
}

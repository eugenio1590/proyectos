package com.prueba.web.mvvm.controladores.seguridad.configuracion;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.prueba.web.configuracion.service.ServicioControlUsuario;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMenu;
import com.prueba.web.model.Menu;
import com.prueba.web.mvvm.AbstractViewModel;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.mvvm.ModelTree;
import com.prueba.web.seguridad.configuracion.service.ServicioControlGrupo;

public class FormularioAccesosViewModel extends AbstractViewModel {
	
	//Servicios
	@BeanInjector("servicioControlUsuario")
	private ServicioControlUsuario servicioControlUsuario;
	
	@BeanInjector("servicioControlGrupo")
	private ServicioControlGrupo servicioControlGrupo;
	
	//GUI
	@Wire("#treeMenuNoAsignado")
	private Tree treeMenuNoAsignado;
	
	@Wire("#pagMenuNoAsignado")
	private Paging pagMenuNoAsignado;
	
	@Wire("#pagMenuAsignado")
	private Paging pagMenuAsignado;
	
	//Modelos
	private ModelTree<Menu> menuNoAsignadoTree;
	private List<Menu> menuNoAsignado;
	
	private ModelTree<GroupMenu> menuAsignadoTree;
	private List<GroupMenu> menuAsignado;
	
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
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("menuNoAsignadoTree")
	public void cambiarMenuNoAsignadoGrupo(@Default("0") int page){
		Map<String, Object> parametros = servicioControlUsuario.consultarRootPadres(page, PAGE_SIZE);
		Integer total = (Integer) parametros.get("total");
		menuNoAsignado = (List<Menu>) parametros.get("menu");
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
		menuAsignado = (List<GroupMenu>) parametros.get("menu");
		menuAsignadoTree = new ModelTree<GroupMenu>(menuAsignado.toArray(new GroupMenu[]{}), true, true, false);
		pagMenuAsignado.setTotalSize(total);
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange({"menuNoAsignadoTree"})
	public void paginarLista(@BindingParam("tipo") int tipo){
		int page;
		switch (tipo) {
		case 1:
			page = pagMenuNoAsignado.getActivePage();
			cambiarMenuNoAsignadoGrupo(page);
			
			break;
			
		case 2:
			page = pagMenuAsignado.getActivePage();
			cambiarMenuAsignadoGrupo(page);
			break;

		default:break;
		}
	}
	
	@Command
	public void openChild(@BindingParam("component") Component component,
			@BindingParam("nodo") DefaultTreeNode<Menu> nodo,
			@BindingParam("wOpen") Boolean wOpen){
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
		
		if(!open)
			openChild(nodo);
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
		Collections.sort(subRamas, new Comparator<Menu>() {
			@Override
			public int compare(Menu m1, Menu m2) {
				// TODO Auto-generated method stub
				return m1.getId().compareTo(m2.getId());
			}
		});
		Menu[] child=subRamas.toArray(new Menu[]{});
		menuNoAsignadoTree.loadChild(nodo, child);
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

	public List<Menu> getMenuNoAsignado() {
		return menuNoAsignado;
	}
	
	public void setMenuNoAsignado(List<Menu> menuNoAsignado) {
		this.menuNoAsignado = menuNoAsignado;
	}
	
	public Group getGrupo() {
		return grupo;
	}
	
	public void setGrupo(Group grupo) {
		this.grupo = grupo;
	}	
}

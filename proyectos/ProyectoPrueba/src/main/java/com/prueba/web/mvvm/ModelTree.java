package com.prueba.web.mvvm;

import java.util.List;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

public class ModelTree<T extends ModelNavbar> extends DefaultTreeModel<T>{
	private static final long serialVersionUID = 1L;
	
	private Boolean justParentRoot = false;
	
	//private T[] data;
	
	@SuppressWarnings("unchecked")
	public ModelTree(T[] data) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}));
		//this.data = data;
		configureModel(getRoot(), data);
	}

	@SuppressWarnings("unchecked")
	public ModelTree(T[] data, boolean emptyChildAsLeaf) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}), emptyChildAsLeaf);
		//this.data = data;
		configureModel(getRoot(), data);
	}
	
	@SuppressWarnings("unchecked")
	public ModelTree(T[] data, boolean emptyChildAsLeaf, boolean multiple) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}), emptyChildAsLeaf);
		//this.data = data;
		configureModel(getRoot(), data);
		setMultiple(multiple);
	}
	
	@SuppressWarnings("unchecked")
	public ModelTree(T[] data, boolean emptyChildAsLeaf, boolean multiple, Boolean justParentRoot) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}), emptyChildAsLeaf);
		//this.data = data;
		this.justParentRoot=justParentRoot;
		configureModel(getRoot(), data);
		setMultiple(multiple);
	}
	
	/**SETTERS Y GETTERS*/
	public Boolean isJustParentRoot() {
		return justParentRoot;
	}

	public void setJustParentRoot(Boolean justParentRoot) {
		this.justParentRoot = justParentRoot;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public void loadChild(TreeNode<T> nodo, T[] child){
		nodo.getChildren().clear();
		configureModel(nodo, child);
	}
	
	@SuppressWarnings("unchecked")
	private void configureModel(TreeNode<T> nodo, T[] data){
		List<TreeNode<T>> children = nodo.getChildren();
		for(T dato : data){
			TreeNode<T>  tNodo = new DefaultTreeNode<T>(dato, new DefaultTreeNode[]{});
			if(dato.getChilds().size()>0 && !justParentRoot)
				configureModel(tNodo, (T[]) dato.childToArray(dato.getClass(), 0));
			else if(dato.getChilds().size()>0)
				tNodo.getChildren().add(new DefaultTreeNode<T>(null));
			children.add(tNodo);
		}
	}
}

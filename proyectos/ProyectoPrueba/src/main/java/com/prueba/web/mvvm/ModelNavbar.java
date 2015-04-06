package com.prueba.web.mvvm;

import java.util.List;

public interface ModelNavbar {
	public int getIdNode();
	public String getLabel();
	public String getIcon();
	public String getUriLocation();
	public ModelNavbar getParent();
	public Boolean isRootParent();
	public List<ModelNavbar> getChilds();
	public List<ModelNavbar> getRootTree();
	public <T> T[] childToArray(Class<?> clazz);
	public <T> T[] childToArray(Class<?> clazz, int element);
}

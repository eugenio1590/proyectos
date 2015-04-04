package com.prueba.web.mvvm;

import java.util.List;

public interface ModelNavbar {
	public String getLabel();
	public String getIcon();
	public String getUriLocation();
	public List<ModelNavbar> getChilds();
	public <T> T[] childToArray(Class<?> clazz);
	public <T> T[] childToArray(Class<?> clazz, int element);
}

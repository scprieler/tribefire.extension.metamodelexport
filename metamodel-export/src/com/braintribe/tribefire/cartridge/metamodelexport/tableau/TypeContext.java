package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

import java.util.ArrayList;
import java.util.List;

public class TypeContext {
	
	public String id;
	public String name;
	public List<PropertyContext> properties = new ArrayList<PropertyContext>();
	public List<PropertyContext> collProperties = new ArrayList<PropertyContext>();
	public Boolean isType = true;
	public String idPropName;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setProperties(List<PropertyContext> properties) {
		this.properties = properties;
	}
	public List<PropertyContext> getProperties() {
		return properties;
	}
	public void setCollProperties(List<PropertyContext> collProperties) {
		this.collProperties = collProperties;
	}
	public List<PropertyContext> getCollProperties() {
		return collProperties;
	}
	public void setIsType(Boolean isType) {
		this.isType = isType;
	}
	public Boolean getIsType() {
		return isType;
	}
	public void setIdPropName(String idPropName) {
		this.idPropName = idPropName;
	}
	public String getIdPropName() {
		return idPropName;
	}

}

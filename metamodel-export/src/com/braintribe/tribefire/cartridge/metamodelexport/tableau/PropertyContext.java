package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

public class PropertyContext {
	
	public String name;
	public String type;
	public Boolean isRefId = false;
	public String refIdName;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public Boolean getIsRefId() {
		return isRefId;
	}
	public void setIsRefId(Boolean isRefId) {
		this.isRefId = isRefId;
	}
	public String getRefIdName() {
		return refIdName;
	}
	public void setRefIdName(String refIdName) {
		this.refIdName = refIdName;
	}

}

package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.tribefire.cartridge.metamodelexport.tableau.TableauExport.JoinContextWrapper;

public class ModelContext {
	
	List<TypeContext> types = new ArrayList<TypeContext>();
	JoinContextWrapper jcw;
	
	public void setTypes(List<TypeContext> types) {
		this.types = types;
	}
	public List<TypeContext> getTypes() {
		return types;
	}
	public void setJcw(JoinContextWrapper jcw) {
		this.jcw = jcw;
	}
	public JoinContextWrapper getJcw() {
		return jcw;
	}

}

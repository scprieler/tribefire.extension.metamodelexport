package com.braintribe.tribefire.cartridge.metamodelexport.common;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.model.meta.GmMetaModel;

public abstract class ModelExport {
	
	public abstract String export(GmMetaModel model, HttpServletRequest req);

}

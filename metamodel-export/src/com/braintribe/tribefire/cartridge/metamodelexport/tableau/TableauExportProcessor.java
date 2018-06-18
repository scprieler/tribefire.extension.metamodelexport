package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

import com.braintribe.model.metamodelexport.deployment.tableau.TableauExportRequest;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.resource.Resource;

public class TableauExportProcessor implements ServiceProcessor<TableauExportRequest, Resource> {

	TableauExport tableauExport;
	
	public void setTableauExport(TableauExport tableauExport) {
		this.tableauExport = tableauExport;
	}
	
	@Override
	public Resource process(ServiceRequestContext requestContext, TableauExportRequest request) {
		return tableauExport.export(request);
	}
	
	
}

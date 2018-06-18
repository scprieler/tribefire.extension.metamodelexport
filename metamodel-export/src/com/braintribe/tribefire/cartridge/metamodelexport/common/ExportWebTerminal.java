package com.braintribe.tribefire.cartridge.metamodelexport.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;

public class ExportWebTerminal extends HttpServlet{
	//http://localhost:8080/tribefire-services/component/entity-export?modelId=model:com.braintribe.model:DocumentModel&output=jse
	//http://localhost:8080/tribefire-services/component/entity-export?modelId=model:com.braintribe.model:DocumentModel&output=svg&viewportSize=500
	
	private static final long serialVersionUID = -2428442671498026105L;
	private static final String MODEL_ID_PARAM = "modelId";
	private static final String OUTPUT_TYPE = "output";
	
	Map<String, ModelExportContext> modelExportContexts = new HashMap<>();
	
	Supplier<PersistenceGmSession> sessionProvider;
	
	public void setSessionProvider(Supplier<PersistenceGmSession> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}
	
	public PersistenceGmSession getSession() {
		return sessionProvider.get();
	}	
	
	public void putModelExport(String key, ModelExportContext mec) {
		modelExportContexts.put(key, mec);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		try {
			String modelId = req.getParameter(MODEL_ID_PARAM);
			String output = req.getParameter(OUTPUT_TYPE);			
			
			ModelExportContext mec = modelExportContexts.get(output);
			if(mec != null) {
				PersistenceGmSession session = getSession();
				
				EntityType<GmMetaModel> type = GmMetaModel.T;
				
				EntityQuery query = EntityQueryBuilder.from(GmMetaModel.T)
						.where()
						.property(type.getIdProperty().getName()).eq(modelId)
						.tc().negation().joker().done();
				
				GmMetaModel gmMetaModel = session.query().entities(query).first();
				if(gmMetaModel == null)
					throw new RuntimeException("no model with id" + modelId + " found!");
				resp.setContentType(mec.mimeType);
				resp.getWriter().write(mec.modelExport.export(gmMetaModel, req));
			}	
			
		} catch (Exception e) {
			e.printStackTrace(resp.getWriter());
		}
		
	}

}

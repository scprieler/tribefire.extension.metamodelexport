package com.braintribe.tribefire.cartridge.metamodelexport.svg;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.modellerfilter.view.ModellerView;
import com.braintribe.model.modellergraph.condensed.CondensedModel;
import com.braintribe.model.modellergraph.condensed.CondensedType;
import com.braintribe.model.modellergraph.graphics.ModelGraphState;
import com.braintribe.model.processing.modellergraph.CondensedModelBuilder;
import com.braintribe.model.processing.modellergraph.ModelGraphConfigurations;
import com.braintribe.model.processing.modellergraph.ModelGraphStateBuilder;
import com.braintribe.model.processing.modellergraph.common.BezierTools;
import com.braintribe.model.processing.modellergraph.common.Complex;
import com.braintribe.model.processing.modellergraph.filter.DefaultFiltering;
import com.braintribe.tribefire.cartridge.metamodelexport.common.FileExport;
import com.braintribe.tribefire.cartridge.metamodelexport.common.ModelExport;

public class SvgExport extends ModelExport{
	//http://localhost:8080/tribefire-services/component/svg?&view=tcdm_default
	
	protected static final String velocityPropertiesName = "velocity.properties";
	
	private VelocityEngine velocityEngine;
	private ModelGraphStateBuilder modelGraphStateBuilder = new ModelGraphStateBuilder();
	private DefaultFiltering defaultFiltering = new DefaultFiltering();
	private BezierTools bezierTools = new BezierTools();
	
	public String export(GmMetaModel model, HttpServletRequest req) {
		try {			
			VelocityContext velocityContext = new VelocityContext();
			ModellerView view = null;
			int viewportSize = Integer.parseInt(req.getParameter("viewportSize"));
			
			CondensedModel condensedModel = CondensedModelBuilder.build(model);
			Map<String, CondensedType> condensedTypes = new HashMap<String, CondensedType>();
			for(CondensedType condensedType : condensedModel.getTypes()){
				condensedTypes.put(condensedType.getGmType().getTypeSignature(), condensedType);
			}
			
			ModelGraphConfigurations modelGraphConfigurations = new ModelGraphConfigurations();
			modelGraphConfigurations.setViewPortDimension(new Complex(viewportSize, viewportSize));
			modelGraphConfigurations.currentFocusedType = view.getFocusedType().getTypeSignature();
			modelGraphConfigurations.modellerView = view;
			defaultFiltering.setModelGraphConfigurations(modelGraphConfigurations);
			modelGraphStateBuilder = new ModelGraphStateBuilder();
			modelGraphStateBuilder.setCondensedTypes(condensedTypes);
			modelGraphStateBuilder.setModelGraphConfigurations(modelGraphConfigurations);
			modelGraphStateBuilder.setRelationshipFilter(defaultFiltering.getFilter(view));
			
			java.util.List<Float> atmoshperesRadii = new ArrayList<Float>();
			float atmosphereCount = 4;
			float firstAtmosphereRadius = (float)(modelGraphConfigurations.focusEntityRadius + 20);
			float atmosphereRadiusStep = 20;			
			for(int i = 1;i<=atmosphereCount;i++){
				atmoshperesRadii.add(firstAtmosphereRadius);
				firstAtmosphereRadius += atmosphereRadiusStep;
			}
			float lastAtmosphereRadius = (float)modelGraphConfigurations.focusToChildEntityDistance.abs();
			atmoshperesRadii.add(lastAtmosphereRadius);
			bezierTools.setAtmoshperesRadii(atmoshperesRadii);
			
			modelGraphStateBuilder.setBezierTools(bezierTools);
			
			ModelGraphState mgs = modelGraphStateBuilder.buildModelGraphState(view.getFocusedType().getTypeSignature());
			
			StringWriter sw = new StringWriter();
			
			velocityContext.put("viewportSize", viewportSize);
			velocityContext.put("mgs", mgs);
			velocityContext.put("RenderUtils", SvgRenderUtils.class);
			
			write("com/braintribe/tribefire/cartridge/metamodelexport/svg/model.html.vm", velocityContext, sw);
			return sw.toString();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return "";				
	}
	
	private void write(String templateName, VelocityContext velocityContext, Writer printWriter) throws Exception {
		VelocityEngine engine = getVelocityEngine();
		engine.mergeTemplate(templateName, "UTF-8", velocityContext, printWriter);
		printWriter.close();
	}
	
	private VelocityEngine getVelocityEngine() throws Exception {
		if (velocityEngine == null) {
			Properties velocityProperties = new Properties();

			try (InputStream in = getClass().getResourceAsStream(velocityPropertiesName)) {
				velocityProperties.load(in);
			} catch (Exception e) {
				throw e;
			}

			velocityEngine = new VelocityEngine(velocityProperties);
		}
		return velocityEngine;
	}

}

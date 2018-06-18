package com.braintribe.tribefire.cartridge.metamodelexport.entity;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.cfg.Configurable;
import com.braintribe.codec.marshaller.api.GmSerializationOptions;
import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.codec.marshaller.api.MarshallerRegistry;
import com.braintribe.codec.marshaller.api.OutputPrettiness;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.tribefire.cartridge.metamodelexport.common.ModelExport;

public class EntityExport extends ModelExport{

	private String codec = "gm/jse";
	private MarshallerRegistry marshallerRegistry;
	
	@Configurable
	public void setMarshallerRegistry(MarshallerRegistry marshallerRegistry) {
		this.marshallerRegistry = marshallerRegistry;
	}
	
	public String export(GmMetaModel model, HttpServletRequest req) {
		try{				
			Marshaller m = marshallerRegistry.getMarshaller(codec);
			if(m == null)
				throw new RuntimeException("no marshaller found for mimeType " + codec);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();		
			
			m.marshall(out, model, GmSerializationOptions.defaults.stabilizeOrder(true).outputPrettiness(OutputPrettiness.high));
			
			String ext = codec.split("/")[1];
			
			return out.toString();
			//return session.resources().create().name(entity.toSelectiveInformation() + "-export." + ext).store(new ByteArrayInputStream(out.toByteArray()));
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

}

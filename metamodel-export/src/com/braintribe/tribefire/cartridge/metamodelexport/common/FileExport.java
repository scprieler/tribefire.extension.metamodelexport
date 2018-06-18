package com.braintribe.tribefire.cartridge.metamodelexport.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.transaction.NestedTransaction;
import com.braintribe.model.resource.Resource;

public abstract class FileExport {
	
	private Supplier<PersistenceGmSession> sessionProvider;
	
	@Configurable
	public void setSessionProvider(Supplier<PersistenceGmSession> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}
	
	public PersistenceGmSession getSession() {
		return sessionProvider.get();
	}	
	
	public Resource createrResource(String fileName, String suffix, String content){
		try {
		Resource resource = null;
		
		PersistenceGmSession session = getSession();
		NestedTransaction nt = session.getTransaction().beginNestedTransaction();
		
		File tmp = File.createTempFile(fileName, suffix);
		
		PrintWriter writer = new PrintWriter(tmp);					
		writer.write(content);
		
		writer.flush();
		writer.close();
		
		String resourceName = fileName + "." + suffix;
		resource = session.resources().create().name(resourceName).store(new FileInputStream(tmp));
		
		System.err.println(session.resources().url(resource).download(true).fileName(resourceName).asString());
		nt.commit();
		
		return resource;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}

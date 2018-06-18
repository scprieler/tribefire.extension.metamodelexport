package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmLinearCollectionType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.GmSimpleType;
import com.braintribe.model.metamodelexport.deployment.tableau.TableauExportRequest;
import com.braintribe.model.resource.Resource;
import com.braintribe.tribefire.cartridge.metamodelexport.common.FileExport;
import com.braintribe.tribefire.cartridge.metamodelexport.tableau.JoinContext.JoinType;

public class TableauExport extends FileExport{
	
	private VelocityEngine velocityEngine;
	protected static final String velocityPropertiesName = "velocity.properties";
	
	public TableauExport() {
		// TODO Auto-generated constructor stub
	}
			
	public Resource export(TableauExportRequest te){
		try {
		Resource resource = null;
		VelocityContext velocityContext = new VelocityContext();
		
		StringWriter sw = new StringWriter();
		
		ModelContext mc = prepareModelContext(te.getModel());
		velocityContext.put("tableauUtil", TableauUtil.class);
		GmEntityType type = te.getModel().entityTypes().filter(candidate -> candidate.getTypeSignature().endsWith("LEAN_Demand")).findFirst().get();
		
		velocityContext.put("connectionContext", prepareConnectionContext(te, prepareJoinContext(type)));	
		velocityContext.put("model", mc);
		velocityContext.put("useConnection", false);
		
		write("com/braintribe/tribefire/cartridge/metamodelexport/tableau/gmWDC.js.vm", velocityContext, sw);
		
		resource = createrResource("gmWDC", "js", sw.toString());
		
		return resource;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private ModelContext prepareModelContext(GmMetaModel model){
		ModelContext mc = new ModelContext();
//		mc.jcw = new JoinContextWrapper();
		int typeCount = 5;
		if(model.entityTypes() != null){
			model.entityTypes().forEach((type) -> {

//				if(type.getTypeSignature().endsWith(".ObjectStatus")){
				if(type.getTypeSignature() != null && type.getProperties() != null && !type.getProperties().isEmpty()){
					TypeContext tc = new TypeContext();
					tc.id = type.getTypeSignature();
					tc.name = type.getTypeSignature().substring(type.getTypeSignature().lastIndexOf(".")+1, type.getTypeSignature().length());
					
					if(type.getProperties() != null){
						for(GmProperty property : type.getProperties()){
							if(property.getType() instanceof GmSimpleType){
								PropertyContext pc = new PropertyContext();
								pc.name = property.getName();
								pc.type = property.getType().getTypeSignature();
								tc.properties.add(pc);
								
								if(property.isId())
									tc.idPropName = property.getName();
							}else if(property.getType() instanceof GmEntityType && !property.getType().getTypeSignature().equals(type.getTypeSignature())){
//								mc.jcw.tableTypes.add(type);
								
								GmEntityType refType = (GmEntityType) property.getType();
								GmProperty idProp = null;
								for(GmProperty candidate : refType.getProperties()){
									if(candidate.isId()){
										idProp = candidate;
										break;
									}
								}
								if(idProp != null){
									PropertyContext pc = new PropertyContext();
									String name = refType.getTypeSignature().substring(refType.getTypeSignature().lastIndexOf(".")+1, 
											refType.getTypeSignature().length());;
									pc.name = property.getName();
									pc.type = idProp.getType().getTypeSignature();
									pc.isRefId = true;
									pc.refIdName = idProp.getName();
									tc.properties.add(pc);
									
//									TypeTuple tt = new TypeTuple();
//									tt.left = type;
//									tt.leftId = property.getName() + "Id";
//									tt.right = refType;
//									tt.rightId = idProp.getName();
//									mc.jcw.relationTuples.add(tt);
								}
							}else if(property.getType() instanceof GmLinearCollectionType && ((GmLinearCollectionType)property.getType()).getElementType() instanceof GmEntityType){
								TypeContext tc2 = prepareCollectionPropertyTypeContext(property);
								if(tc2 != null){
									PropertyContext pc = new PropertyContext();
									mc.types.add(tc2);
									pc.name = property.getName();
									tc.collProperties.add(pc);
								}
							}
						}
					}
					if(!tc.properties.isEmpty())
						mc.types.add(tc);
				}
			});
		}
		
		return mc;
	}
	
	class JoinContextWrapper{
//		public Set<GmEntityType> tableTypes = new HashSet<GmEntityType>();
		public Set<TypeTuple> relationTuples = new HashSet<TypeTuple>();
	}
	
	class TypeTuple {
		public GmEntityType left;
		public String leftId;
		public GmEntityType right;
		public String rightId;
	}
	
	private JoinContextWrapper prepareJoinContext(GmEntityType rootType){
		HashSet<GmEntityType> cache = new HashSet<GmEntityType>();
		if(rootType != null){
			JoinContextWrapper jcw = new JoinContextWrapper();				
			
			cache.add(rootType);
			jcw.relationTuples = getTTS(cache, rootType);
			
			return jcw;
		}
		return null;
	}
	
	private Set<TypeTuple> getTTS(HashSet<GmEntityType> cache, GmEntityType rootType){
		Set<TypeTuple> tts = new HashSet<TypeTuple>();
		
		for(GmProperty gmProperty : rootType.getProperties()){
			if(gmProperty.getType() instanceof GmEntityType){
				GmEntityType refType = (GmEntityType)gmProperty.getType();
				
				if(!refType.getTypeSignature().equals(rootType.getTypeSignature())){
				GmProperty idProp = null;
				for(GmProperty candidate : refType.getProperties()){
					if(candidate.isId()){
						idProp = candidate;
						break;
					}
				}					
				if(idProp != null){
					TypeTuple tt = new TypeTuple();
					tt.left = rootType;
					tt.leftId = gmProperty.getName() + "Id";
					tt.right = refType;
					tt.rightId = idProp.getName();
					tts.add(tt);
				}
				}
				
				if(!cache.contains(refType)){
					cache.add(refType);
					tts.addAll(getTTS(cache, refType));
				}				
			}
		}
		return tts;
	}
	
	private ConnectionContext prepareConnectionContext(TableauExportRequest te, JoinContextWrapper jcw){		
		
		ConnectionContext cc = new ConnectionContext();
		cc.port = te.getPort();
		cc.host = te.getHost();
		cc.tfUrl = te.getTfUrl();
		cc.accessId = te.getAccessId();
		cc.alias = te.getDefaultJoinName();
		cc.name = te.getConnectionName();
		
		if(jcw != null){
//			Set<GmEntityType> tableTypes = jcw.tableTypes;
			Set<TypeTuple> relationTuples = jcw.relationTuples;
			
//			for(GmEntityType type : tableTypes){
//				TableContext tc = new TableContext();
//				tc.alias = type.getTypeSignature().substring(type.getTypeSignature().lastIndexOf(".")+1, type.getTypeSignature().length());
//				tc.id = type.getTypeSignature().replaceAll("\\.", "_");
//				cc.tables.add(tc);
//			}
			
			for(TypeTuple tt : relationTuples){
				JoinContext jc = new JoinContext();
				
				String alias = tt.left.getTypeSignature().substring(tt.left.getTypeSignature().lastIndexOf(".")+1, 
						tt.left.getTypeSignature().length()) + "_per_" + tt.leftId + "_and_" + tt.rightId;
				TableContext ltc = new TableContext();
				ltc.alias = alias;
				ltc.id = tt.leftId;
				jc.left = ltc;
				
				TableContext tc = new TableContext();
				tc.alias = alias;
				tc.id = tt.left.getTypeSignature().replaceAll("\\.", "_");
				cc.tables.add(tc);
				
				alias = tt.right.getTypeSignature().substring(tt.right.getTypeSignature().lastIndexOf(".")+1, 
						tt.right.getTypeSignature().length()) + "_per_" + tt.rightId + "_and_" + tt.leftId;
				TableContext rtc = new TableContext();
				rtc.alias = alias;
				rtc.id = tt.rightId;
				jc.right = rtc;
				
				tc = new TableContext();
				tc.alias = alias;
				tc.id = tt.right.getTypeSignature().replaceAll("\\.", "_");
				cc.tables.add(tc);
				
				jc.type = JoinType.left;
				
				cc.joins.add(jc);
			}
		}
		
		return cc;
	}
	
	private TypeContext prepareCollectionPropertyTypeContext(GmProperty collectionProperty){
		
		TypeContext tc = new TypeContext();
				
		GmEntityType parentType = collectionProperty.getDeclaringType();
		String parentShortName = parentType.getTypeSignature().substring(parentType.getTypeSignature().lastIndexOf(".")+1, parentType.getTypeSignature().length());
		GmLinearCollectionType collectionType = (GmLinearCollectionType) collectionProperty.getType();
		GmEntityType childType = (GmEntityType) collectionType.getElementType();
		String childShortName = childType.getTypeSignature().substring(childType.getTypeSignature().lastIndexOf(".")+1, childType.getTypeSignature().length());
		String name = collectionProperty.getName();
		
		tc.id = parentType.getTypeSignature()+"."+name;
		tc.name = parentShortName.toLowerCase() + "_" + name;
		tc.isType = false;
		
		PropertyContext pkc = new PropertyContext();
		pkc.name = parentShortName.toLowerCase();
		pkc.isRefId = true;		
		
		GmProperty idProp = null;
		for(GmProperty candidate : parentType.getProperties()){
			if(candidate.isId()){
				idProp = candidate;
				break;
			}
		}
		if(idProp == null)
			return null;
		pkc.type = idProp.getType().getTypeSignature();		
		pkc.refIdName = idProp.getName();
		
		tc.properties.add(pkc);
		
		PropertyContext fkc = new PropertyContext();
		fkc.name = childShortName.toLowerCase();
		fkc.isRefId = true;

		idProp = null;
		for(GmProperty candidate : childType.getProperties()){
			if(candidate.isId()){
				idProp = candidate;
				break;
			}
		}
		if(idProp == null)
			return null;
		
		fkc.type = idProp.getType().getTypeSignature();		
		fkc.refIdName = idProp.getName();
		
		tc.properties.add(fkc);
		
		return tc;
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

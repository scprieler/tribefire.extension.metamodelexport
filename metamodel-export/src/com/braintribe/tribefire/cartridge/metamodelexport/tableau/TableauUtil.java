package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TableauUtil {
	
	public static String getDataType(String type){
		if(type != null){
		if(type.equals("string"))
			return "tableau.dataTypeEnum.string";
		if(type.equals("boolean"))
			return "tableau.dataTypeEnum.bool";
		if(type.equals("integer") || type.equals("long"))
			return "tableau.dataTypeEnum.int";
		if(type.equals("float") || type.equals("double"))
			return "tableau.dataTypeEnum.float";
		if(type.equals("date"))
			return "tableau.dataTypeEnum.datetime";
		if(type.equals("datetime"))
			return "tableau.dataTypeEnum.datetime";
		else
			return "tableau.dataTypeEnum.string";
		}
		else
			return "tableau.dataTypeEnum.string";
	}
	
	public static String createDataUrl(ConnectionContext cc){
		return "http://" + cc.host + ":" + cc.port + "/" + cc.tfUrl + "/rest/fetch";
	}
	
	public static String createLoginUrl(ConnectionContext cc){
		return "http://" + cc.host + ":" + cc.port + "/" + cc.tfUrl + "/rest/authenticate";
	}
	
	public static String replace(String str){
		return str.replaceAll("\\.", "_");
	}
	
}

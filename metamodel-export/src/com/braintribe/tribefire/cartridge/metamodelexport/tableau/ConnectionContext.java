package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

import java.util.ArrayList;
import java.util.List;

public class ConnectionContext {
	
	public String host;
	public String port;
	public String tfUrl;
	public String accessId;
	public String alias;
	public String name;
	
	public List<TableContext> tables = new ArrayList<TableContext>();
	public List<JoinContext> joins = new ArrayList<JoinContext>();
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getTfUrl() {
		return tfUrl;
	}
	public void setTfUrl(String tfUrl) {
		this.tfUrl = tfUrl;
	}
	public String getAccessId() {
		return accessId;
	}
	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public List<TableContext> getTables() {
		return tables;
	}
	public void setTables(List<TableContext> tables) {
		this.tables = tables;
	}
	public List<JoinContext> getJoins() {
		return joins;
	}
	public void setJoins(List<JoinContext> joins) {
		this.joins = joins;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	

}

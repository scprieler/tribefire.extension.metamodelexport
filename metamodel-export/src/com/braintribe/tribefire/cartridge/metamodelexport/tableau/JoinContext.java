package com.braintribe.tribefire.cartridge.metamodelexport.tableau;

public class JoinContext {
	
	enum JoinType{left, right, inner}
	
	public String alias;
	public String id;
	public TableContext left;
	public TableContext right;
	public JoinType type;
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TableContext getLeft() {
		return left;
	}
	public void setLeft(TableContext left) {
		this.left = left;
	}
	public TableContext getRight() {
		return right;
	}
	public void setRight(TableContext right) {
		this.right = right;
	}
	public JoinType getType() {
		return type;
	}
	public void setType(JoinType type) {
		this.type = type;
	}

	
}

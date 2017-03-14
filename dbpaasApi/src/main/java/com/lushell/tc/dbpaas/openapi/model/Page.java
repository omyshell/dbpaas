package com.lushell.tc.dbpaas.openapi.model;

public class Page {
    private int currentPage=1;
    private int rowsPerPage=10;
    private int rowsCount;
    private int totalPage;
    private String enviroment;
    
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	public int getRowsCount() {
		return rowsCount;
	}
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public String getEnviroment() {
		return enviroment;
	}
	public void setEnviroment(String env) {
		this.enviroment = env;
	}
    
    
}

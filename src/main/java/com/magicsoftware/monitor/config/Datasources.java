package com.magicsoftware.monitor.config;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datasources")
public class Datasources {

	private Datasource datasource;

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

}

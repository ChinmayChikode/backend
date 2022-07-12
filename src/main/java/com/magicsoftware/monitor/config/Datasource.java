package com.magicsoftware.monitor.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datasource")
public class Datasource {

	public String getDialect() {
		return dialect;
	}

	@XmlAttribute(name = "hibernate.dialect")
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	@XmlAttribute
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	@XmlAttribute
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	@XmlAttribute
	public void setPassword(String password) {
		this.password = password;
	}

	private String dialect;

	private String driverClassName;

	private String url;

	private String username;

	private String password;

	@Override
	public String toString() {
		return "Datasource [dialect=" + dialect + ", driverClassName=" + driverClassName + ", url=" + url + ", username=" + username + ", password=" + password
				+ "]";
	}

}

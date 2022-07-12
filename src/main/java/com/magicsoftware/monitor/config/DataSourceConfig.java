package com.magicsoftware.monitor.config;

import java.io.File;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

	@Value("${datasource.file.name}")
	private String datasourceFile;

	@Bean
	@Primary
	public DataSource dataSource() {
		Datasources datasources = loadDatasourceFromFile();
		return DataSourceBuilder.create().username(datasources.getDatasource().getUsername()).password(datasources.getDatasource().getPassword())
				.url(datasources.getDatasource().getUrl()).driverClassName(datasources.getDatasource().getDriverClassName()).build();
	}

	private Datasources loadDatasourceFromFile() {
		Datasources datasources = null;
		try {
			File file = new File(datasourceFile);
			JAXBContext jaxbContext = JAXBContext.newInstance(Datasources.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			datasources = (Datasources) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace(); // TODO ganesh :-need to change it by logger 
		}
		return datasources;
	}
}
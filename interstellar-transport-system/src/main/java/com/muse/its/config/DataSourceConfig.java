package com.muse.its.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	private static final Logger log = LogManager.getLogger(DataSourceConfig.class);
	
	@Bean
	public DataSource dataSource() {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setConnectionAttributes("create=true");
		dataSource.setDatabaseName("itsdb");
		dataSource.setUser("root");
		dataSource.setPassword("root");
		
		try {
			dataSource.getConnection();
		} catch (SQLException e) {
			log.error("Failed to connect to the database: " + e.getMessage());
			e.printStackTrace();
		}
		return dataSource;
	}
}
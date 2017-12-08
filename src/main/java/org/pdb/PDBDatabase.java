package org.pdb;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
public class PDBDatabase extends SpringBootServletInitializer {

	public static void main(String[] args) {
		startH2Server();
		SpringApplication.run(PDBDatabase.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		startH2Server();
		return application.sources(PDBDatabase.class);
	}

	@Bean
	@ConfigurationProperties("spring.datasource")
	public HikariDataSource datasource() {
		return (HikariDataSource) DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	private static void startH2Server() {
		try {
			Server h2Server = Server.createTcpServer().start();
			if (h2Server.isRunning(true)) {
				System.out.println("H2 server was started and is running.");

			} else {
				throw new RuntimeException("Could not start H2 server.");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to start H2 server: ", e);
		}
	}

}

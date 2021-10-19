package com.pmic.apachecameldemo.config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class OrderManagementConfig {

	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	public ConnectionFactory pmicRabbitConnectionFactory(CachingConnectionFactory connectionFactory)
			throws IOException, TimeoutException {
		// refer
		// https://stackoverflow.com/questions/55022648/spring-boot-2-connection-to-rabbitmq-via-apache-camel
		// for more details

		// Details from above link - Reason for creating bean of type
		// com.rabbitmq.client.ConnectionFactory :
		// The problem appears to be that RabbitMQComponent is expecting to find a
		// connection factory of type com.rabbitmq.client.ConnectionFactory.

		// However, the springboot auto-configure is creating a connection factory of
		// type org.springframework.amqp.rabbit.connection.CachingConnectionFactory.

		// So, whenever the RabbitMQComponent attempts to find the appropriate
		// connection factory, because it is looking for the specific type, and because
		// it does not subclass the rabbitmq ConnectionFactory, it returns a null value,
		// and fails to use the appropriate host name and configuration parameters
		// specified in your application.yml.
		return connectionFactory.getRabbitConnectionFactory();

		// or create whole bean like below if the connection parameters are different
		// than provided in the application.yml file
//		ConnectionFactory connectionFactory = new ConnectionFactory();
//		connectionFactory.setHost("localhost");
//		connectionFactory.setPort(5672);
//		connectionFactory.setUsername("guest");
//		connectionFactory.setPassword("guest");
//		return connectionFactory;
	}
}

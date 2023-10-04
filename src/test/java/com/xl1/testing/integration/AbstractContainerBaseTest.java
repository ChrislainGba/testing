package com.xl1.testing.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractContainerBaseTest {
	static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;
	// will be started before and stopped after each test method
	//static bloc is executed while en class is loaded
	static {
		POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
				.withDatabaseName("DB_TESTING")
		        .withUsername("postgres")
		        .withPassword("postgres");
		
		POSTGRE_SQL_CONTAINER.start();
	}
	
    
    //Add dynamic properties ton context From container to context
    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
    	registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
    	registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
    	registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    	registry.add("spring.datasource.driver-class-name", POSTGRE_SQL_CONTAINER::getDriverClassName);
    }
}

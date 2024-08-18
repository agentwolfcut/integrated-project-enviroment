package dev.backendintegratedproject.configs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "dev.backendintegratedproject.repositories",
        entityManagerFactoryRef = "taskEntityManagerFactory",
        transactionManagerRef = "taskTransactionManager")
public class TaskDBConfig {

    @Bean(name = "taskDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "taskEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean taskEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("taskDataSource") DataSource dataSource) { // Fixed Qualifier
        return builder
                .dataSource(dataSource)
                .packages("dev.backendintegratedproject.entities")
                .persistenceUnit("task")
                .build();
    }

    @Bean(name = "taskTransactionManager")
    public PlatformTransactionManager taskTransactionManager(
            @Qualifier("taskEntityManagerFactory") LocalContainerEntityManagerFactoryBean taskEntityManagerFactory) {
        return new JpaTransactionManager(taskEntityManagerFactory.getObject());
    }
}
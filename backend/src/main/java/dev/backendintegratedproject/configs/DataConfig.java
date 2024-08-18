package dev.backendintegratedproject.configs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
public class DataConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties().initializeDataSourceBuilder()
                .type(DriverManagerDataSource.class).build();
    }

    @Bean
    @Qualifier("loginDataSource")
    @ConfigurationProperties("spring.login.datasource")
    public DataSourceProperties loginDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier("loginDataSource")
    public DataSource loginDataSource() {
        return loginDataSourceProperties().initializeDataSourceBuilder()
                .type(DriverManagerDataSource.class).build();
    }

    @Bean
    @Qualifier("loginEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean loginEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(loginDataSource())
                .packages("dev.backendintegratedproject.entities")
                .persistenceUnit("login")
                .build();
    }

    @Bean
    @Qualifier("loginTransactionManager")
    public PlatformTransactionManager loginTransactionManager(
            final @Qualifier("loginEntityManagerFactory") LocalContainerEntityManagerFactoryBean loginEntityManagerFactory) {
        return new JpaTransactionManager(loginEntityManagerFactory.getObject());
    }
}
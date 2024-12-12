package dev.backendintegratedproject.config;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "dev.backendintegratedproject.primarydatasource.repositories", entityManagerFactoryRef = "primaryEntityMangerFactory", transactionManagerRef = "primaryTransactionManager")
public class PrimaryDatasourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "project.datasource")
    public DataSourceProperties primaryDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public HikariDataSource primaryHikariDataSource(){
        return primaryDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean("primaryEntityMangerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryLocalContainerEntityManagerFactoryBean(){
        LocalContainerEntityManagerFactoryBean lcemf = new LocalContainerEntityManagerFactoryBean();
        lcemf.setDataSource(primaryHikariDataSource());
        HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
        lcemf.setJpaVendorAdapter(hjva);
        lcemf.setPackagesToScan("dev.backendintegratedproject.primarydatasource.entities");
        return lcemf;
    }

    @Bean("primaryTransactionManager")
    @Primary
    public PlatformTransactionManager primaryPlatformTransactionManager(@Qualifier("primaryEntityMangerFactory") EntityManagerFactory primaryEntityManagerFactory){
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }

}

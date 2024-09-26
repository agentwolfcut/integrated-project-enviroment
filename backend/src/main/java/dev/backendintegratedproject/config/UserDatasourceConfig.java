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
@EnableJpaRepositories(basePackages = "dev.backendintegratedproject.userdatasource.repositories", entityManagerFactoryRef = "secondaryEntityMangerFactory", transactionManagerRef = "secondaryTransactionManager")
public class UserDatasourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.userdatasource")
    public DataSourceProperties secondaryDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource secondaryHikariDataSource(){
        return secondaryDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean("secondaryEntityMangerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryLocalContainerEntityManagerFactoryBean(){
        LocalContainerEntityManagerFactoryBean lcemf = new LocalContainerEntityManagerFactoryBean();
        lcemf.setDataSource(secondaryHikariDataSource());
        HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
        lcemf.setJpaVendorAdapter(hjva);
        lcemf.setPackagesToScan("dev.backendintegratedproject.userdatasource.entities");
        return lcemf;
    }

    @Bean("secondaryTransactionManager")
    public PlatformTransactionManager secondaryPlatformTransactionManager(@Qualifier("secondaryEntityMangerFactory") EntityManagerFactory primaryEntityManagerFactory){
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }

}

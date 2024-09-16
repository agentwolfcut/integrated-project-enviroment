package dev.backendintegratedproject.configs;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "dev.backendintegratedproject.managements",
        entityManagerFactoryRef = "projectManagementEntityManager",
        transactionManagerRef = "projectManagementTransactionManager"
)
public class DataConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties projectManagementDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.configuration")
    public DataSource projectManagementDataSource() {
        return projectManagementDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "projectManagementEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean projectManagementEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {

        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(projectManagementDataSource())
                .packages("dev.backendintegratedproject.managements")
                .properties(jpaProperties)
                .build();
    }

    @Bean(name = "projectManagementTransactionManager")
    @Primary
    public PlatformTransactionManager projectManagementTransactionManager(
            final @Qualifier("projectManagementEntityManager") LocalContainerEntityManagerFactoryBean projectManagementEntityManager) {
        return new JpaTransactionManager(
                projectManagementEntityManager.getObject()
        );
    }
}

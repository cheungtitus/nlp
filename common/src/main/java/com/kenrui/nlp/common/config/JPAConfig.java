package com.kenrui.nlp.common.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.kenrui.nlp.common.repositories")
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
@ComponentScan("com.kenrui.nlp.common") // required for MBeanExporter
public class JPAConfig {
    @Autowired private Environment env;
    @Autowired MBeanExporter mBeanExporter;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(getJpaVendorAdapter());
        em.setDataSource(getDataSource());
        em.setPersistenceUnitName("NLP_PU");
        em.setPackagesToScan(new String[] {"com.kenrui.nlp.common"});
        em.setJpaProperties(jpaProperties());
        return em;
    }

    @Bean
    public JpaVendorAdapter getJpaVendorAdapter() {
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        return adapter;
    }

    @Bean
    public DataSource getDataSource() {
        // https://stackoverflow.com/questions/28295503/migration-to-tomcat-8-instancealreadyexistsexception-datasource
        mBeanExporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("database.driverClassName"));
        dataSource.setUrl(env.getProperty("database.url"));
        dataSource.setUsername(env.getProperty("database.username"));
        dataSource.setPassword(env.getProperty("database.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        properties.put("javax.persistence.schema-generation.database.action", env.getProperty("javax.persistence.schema-generation.database.action"));
        properties.put("javax.persistence.schema-generation.scripts.action", env.getProperty("javax.persistence.schema-generation.scripts.action"));
        properties.put("javax.persistence.schema-generation.create-database-schemas", env.getProperty("javax.persistence.schema-generation.create-database-schemas"));
        properties.put("javax.persistence.schema-generation.scripts.drop-target", env.getProperty("javax.persistence.schema-generation.scripts.drop-target"));
        properties.put("javax.persistence.schema-generation.scripts.create-target", env.getProperty("javax.persistence.schema-generation.scripts.create-target"));
        return properties;
    }
}

package com.websystique.springsecurity.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.websystique.springsecurity.configuration" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.websystique.springsecurity.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
     }

    @Bean
    DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        if (environment.containsProperty("MYSQL_DB_HOST")) {
            dataSource.setJdbcUrl("jdbc:mysql://"
                    + environment.getProperty("MYSQL_DB_HOST") + ":"
                    + environment.getProperty("MYSQL_DB_PORT") + "/"
                    + environment.getProperty("openshift.database"));
            dataSource.setDriverClassName(environment.getProperty("openshift.driverClassName"));
            dataSource.setUsername(environment.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(environment.getProperty("MYSQL_DB_PASSWORD"));

        } else {

            dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
            dataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
            dataSource.setUsername(environment.getProperty("jdbc.username"));
            dataSource.setPassword(environment.getProperty("jdbc.password"));
        }
        return dataSource;
    }
    /*
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if (environment.getProperty("MYSQL_DATABASE") != null) {
            //
            // running in openshift server
            String host = "172.30.160.218";
            String port = "3306";
            String dbName = environment.getProperty("MYSQL_DATABASE");
            String dbUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
            dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
            dataSource.setUrl(dbUrl);
            dataSource.setUsername(environment.getProperty("MYSQL_USER"));
            dataSource.setPassword(environment.getProperty("MYSQL_PASSWORD"));
        }
        else {
            dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
            dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
            dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
            dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        }
        return dataSource;
    }
*/
     /*
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if (environment.getProperty("OPENSHIFT_APP_NAME") != null) {
            //
            // running in openshift server
            String host = environment.getProperty("OPENSHIFT_MYSQL_DB_HOST");
            String port = environment.getProperty("OPENSHIFT_MYSQL_DB_PORT");
            String dbName = environment.getProperty("OPENSHIFT_APP_NAME");
            String dbUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);

            dataSource.setUrl(dbUrl);
            dataSource.setUsername(environment.getProperty("OPENSHIFT_MYSQL_DB_USERNAME"));
            dataSource.setPassword(environment.getProperty("OPENSHIFT_MYSQL_DB_PASSWORD"));
        }
        else {
            dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
            dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
            dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
            dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        }
        return dataSource;
    }
    */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;        
    }
    
	@Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }
}


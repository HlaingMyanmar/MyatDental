package org.sspd.myatdental.Configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement()
@ComponentScans({
        @ComponentScan(basePackages = "org.sspd.myatdental")


})
public class AppConfig {

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setMessageInterpolator(new org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator());
        return validatorFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        // Database connection settings
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/mdcdb");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // HikariCP specific settings
        hikariConfig.setMaximumPoolSize(100);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setMaxLifetime(1800000);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(
                "org.sspd.myatdental"
        );
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.jdbc.batch_size", "100");
        properties.put("hibernate.transaction.jta.platform", "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform");
        properties.put("hibernate.order_inserts", "true");
        properties.put("hibernate.order_updates", "true");




        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }
}

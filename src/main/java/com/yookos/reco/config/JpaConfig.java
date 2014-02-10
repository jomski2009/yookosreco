package com.yookos.reco.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Created by jome on 2014/02/06.
 */

@Configuration
@EnableJpaRepositories("com.yookos.reco.jpa")
public class JpaConfig {

    @Bean
    public BoneCPDataSource dataSource() {
        BoneCPDataSource ds = new BoneCPDataSource();
        ds.setDriverClass("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/yookos3");//postgresql needed here
        ds.setUsername("postgres");
        ds.setPassword("wordpass15");
        ds.setIdleConnectionTestPeriodInMinutes(60);
        ds.setIdleMaxAgeInMinutes(420);
        ds.setMaxConnectionsPerPartition(30);
        ds.setMinConnectionsPerPartition(10);
        ds.setPartitionCount(3);
        ds.setAcquireIncrement(5);
        ds.setStatementsCacheSize(100);
        ds.setReleaseHelperThreads(3);

        return ds;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hva = new HibernateJpaVendorAdapter();
        hva.setShowSql(false);
        hva.setGenerateDdl(false);
        hva.setDatabase(Database.POSTGRESQL);
        return hva;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(BoneCPDataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("com.yookos.reco.jpa");
        return lef;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }


}

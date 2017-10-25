package com.viettel.fw;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jmx.support.MBeanServerFactoryBean;

import javax.management.MBeanServer;

/**
 * @author nhannt34
 * @since 17/12/2015
 */
@Configuration
public class CacheConfiguration {
    @Bean
    public CacheManager cacheManager() {
        return ehCacheManagerFactoryBean().getObject();
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        cmfb.afterPropertiesSet();
        return cmfb;
    }

    @Bean
    public ManagementService managementService() {
        ManagementService managementService = new ManagementService(ehCacheManagerFactoryBean().getObject(), mBeanServer(), true, true, true, true);
        managementService.init();
        return managementService;
    }

//    @Bean
//    //@DependsOn("mBeanServerFactory")
//    public MBeanExporter jmxBeanRegister() throws Exception {
//
//        MBeanExporter mbeanExporter = new MBeanExporter();
//        mbeanExporter.setServer(mBeanServer());
//        Map map = new HashMap<>();
//        map.put("atomikos:name=tx-service", jmxTransactionService());
//        //map.put("hibernate:type=statistics", jmxHibernateStatisticsMBean());
//        mbeanExporter.setBeans(map);
//        return mbeanExporter;
//    }
//
//    @Bean
//    public JmxTransactionService jmxTransactionService() throws Exception {
//        JmxTransactionService jmxTransactionService = new JmxTransactionService();
//        jmxTransactionService.preRegister(mBeanServer(), null);
//        return jmxTransactionService;
//    }

    /*
    @Bean
    public org.hibernate.jmx.HibernateStatisticsFactoryBean jmxHibernateStatisticsMBean() {
        HibernateStatisticsFactoryBean bean = new HibernateStatisticsFactoryBean();
        EntityManagerFactory emfSale =  ApplicationContextProvider.getApplicationContext().getBean("entityManagerFactorySale", EntityManagerFactory.class);
        SessionFactory sessionFactory = emfSale.unwrap(SessionFactory.class);
        bean.setSessionFactory(sessionFactory);

        return bean;
    }*/

    @Bean
    //@DependsOn("transactionManager")
    public MBeanServerFactoryBean mBeanServerFactory() {
        MBeanServerFactoryBean mBeanServerFactory = new MBeanServerFactoryBean();
        mBeanServerFactory.setLocateExistingServerIfPossible(true);
        mBeanServerFactory.afterPropertiesSet();
        return mBeanServerFactory;
    }

    @Bean
    public MBeanServer mBeanServer() {
        return mBeanServerFactory().getObject();
    }
}

//
//
///**
// * @author nhannt34
// * @since 17/12/2015
// */
//@Configuration
//public class CacheConfiguration {
//    @Bean
//    public CacheManager cacheManager() {
//        return ehCacheManagerFactoryBean().getObject();
//    }
//
//    @Bean
//    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
//        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
//        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        cmfb.setShared(true);
//        cmfb.afterPropertiesSet();
//        return cmfb;
//    }
//
//    @Bean
//    public ManagementService managementService() {
//        ManagementService managementService = new ManagementService(ehCacheManagerFactoryBean().getObject(), mBeanServer(), true, true, true, true);
//        managementService.init();
//        return managementService;
//    }
//
//    @Bean
//    public MBeanExporter mbeanExporter() {
//        MBeanExporter mbeanExporter = new MBeanExporter();
//        mbeanExporter.setServer(mBeanServer());
//        return mbeanExporter;
//    }
//
//    @Bean
//    public JmxTransactionService jmxTransactionService() throws Exception {
//        JmxTransactionService jmxTransactionService = new JmxTransactionService();
//        jmxTransactionService.setHeuristicsOnly(false);
//
//        ObjectName objectName = jmxTransactionService.preRegister(mBeanServer(), null);
//        mBeanServer().registerMBean(jmxTransactionService, objectName);
//
//        return jmxTransactionService;
//    }
//
//    @Bean
//    public MBeanServerFactoryBean mBeanServerFactory() {
//        MBeanServerFactoryBean mBeanServerFactory = new MBeanServerFactoryBean();
//        mBeanServerFactory.setLocateExistingServerIfPossible(true);
//        mBeanServerFactory.afterPropertiesSet();
//        return mBeanServerFactory;
//    }
//
//    @Bean
//    public MBeanServer mBeanServer() {
//        MBeanServer mbeanServer = mBeanServerFactory().getObject();
//        return mbeanServer;
//    }
//}

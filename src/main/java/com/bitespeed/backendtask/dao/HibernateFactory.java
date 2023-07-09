package com.bitespeed.backendtask.dao;

import com.bitespeed.backendtask.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateFactory {
    private static Session session = null;
    public static Session getHibernateSession()
    {
        if(session == null) {
            Configuration cfg = new Configuration()
                    .configure()
                    .addAnnotatedClass(Order.class)
                    .addAnnotatedClass(OrderDao.class);
            StandardServiceRegistryBuilder standardServiceRegistryBuilder =
                    new StandardServiceRegistryBuilder()
                            .applySettings(cfg.getProperties());
            StandardServiceRegistry serviceRegistry =
                    standardServiceRegistryBuilder.build();
            SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
            session = sessionFactory.openSession();
        }
        return  session;
    }
}

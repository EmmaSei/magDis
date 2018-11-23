package com.seiranyan.jsonpostgres.utils;


import com.seiranyan.jsonpostgres.entities.Area;
import com.seiranyan.jsonpostgres.entities.Specialization;
import com.seiranyan.jsonpostgres.entities.Vacancy;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Vacancy.class);
                configuration.addAnnotatedClass(Area.class);
                configuration.addAnnotatedClass(Specialization.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MainApp {

    public static void main(String[] args) {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();

        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        // Insert multiple products
        Product p1 = new Product("Laptop", "Electronics", 55000, 10);
        Product p2 = new Product("Phone", "Electronics", 25000, 15);
        Product p3 = new Product("TV", "Electronics", 45000, 5);
        Product p4 = new Product("Shoes", "Fashion", 3000, 20);
        Product p5 = new Product("Watch", "Fashion", 5000, 0);
        Product p6 = new Product("Book", "Education", 800, 50);

        session.persist(p1);
        session.persist(p2);
        session.persist(p3);
        session.persist(p4);
        session.persist(p5);
        session.persist(p6);

        tx.commit();

        session.close();
        factory.close();

        System.out.println("Products saved successfully!");
    }
}

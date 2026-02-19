package com.example;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class HQLDemo {

    public static void main(String[] args) {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();

        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        // =============================
        // 1️⃣ SORTING
        // =============================

        System.out.println("Price ASC:");
        session.createQuery("FROM Product ORDER BY price ASC", Product.class)
                .list()
                .forEach(System.out::println);

        System.out.println("\nPrice DESC:");
        session.createQuery("FROM Product ORDER BY price DESC", Product.class)
                .list()
                .forEach(System.out::println);

        System.out.println("\nQuantity Highest First:");
        session.createQuery("FROM Product ORDER BY quantity DESC", Product.class)
                .list()
                .forEach(System.out::println);

        // =============================
        // 2️⃣ PAGINATION
        // =============================

        System.out.println("\nFirst 3 Products:");
        Query<Product> page1 = session.createQuery("FROM Product", Product.class);
        page1.setFirstResult(0);
        page1.setMaxResults(3);
        page1.list().forEach(System.out::println);

        System.out.println("\nNext 3 Products:");
        Query<Product> page2 = session.createQuery("FROM Product", Product.class);
        page2.setFirstResult(3);
        page2.setMaxResults(3);
        page2.list().forEach(System.out::println);

        // =============================
        // 3️⃣ AGGREGATE FUNCTIONS
        // =============================

        Long total = session.createQuery(
                "SELECT COUNT(*) FROM Product",
                Long.class).uniqueResult();
        System.out.println("\nTotal Products: " + total);

        Long available = session.createQuery(
                "SELECT COUNT(*) FROM Product WHERE quantity > 0",
                Long.class).uniqueResult();
        System.out.println("Available Products: " + available);

        Object[] minMax = session.createQuery(
                "SELECT MIN(price), MAX(price) FROM Product",
                Object[].class).uniqueResult();
        System.out.println("Min Price: " + minMax[0]);
        System.out.println("Max Price: " + minMax[1]);

        // =============================
        // 4️⃣ GROUP BY
        // =============================

        System.out.println("\nCount by Description:");
        List<Object[]> groupData = session.createQuery(
                "SELECT description, COUNT(*) FROM Product GROUP BY description",
                Object[].class).list();

        for (Object[] row : groupData) {
            System.out.println(row[0] + " -> " + row[1]);
        }

        // =============================
        // 5️⃣ WHERE (Price Range)  ✅ FIXED
        // =============================

        System.out.println("\nProducts between 1000 and 50000:");
        session.createQuery(
                "FROM Product WHERE price BETWEEN :min AND :max",
                Product.class)
                .setParameter("min", 1000.0)   // 🔥 Double value
                .setParameter("max", 50000.0)  // 🔥 Double value
                .list()
                .forEach(System.out::println);

        // =============================
        // 6️⃣ LIKE QUERIES
        // =============================

        System.out.println("\nNames starting with 'L':");
        session.createQuery("FROM Product WHERE name LIKE 'L%'", Product.class)
                .list()
                .forEach(System.out::println);

        System.out.println("\nNames ending with 'e':");
        session.createQuery("FROM Product WHERE name LIKE '%e'", Product.class)
                .list()
                .forEach(System.out::println);

        System.out.println("\nNames containing 'oo':");
        session.createQuery("FROM Product WHERE name LIKE '%oo%'", Product.class)
                .list()
                .forEach(System.out::println);

        System.out.println("\nNames with length = 5:");
        session.createQuery("FROM Product WHERE LENGTH(name) = 5", Product.class)
                .list()
                .forEach(System.out::println);

        tx.commit();
        session.close();
        factory.close();
    }
}

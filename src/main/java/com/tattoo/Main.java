package com.tattoo;

import com.tattoo.config.HibernateUtil;
import com.tattoo.network.Server;
import com.tattoo.utils.DataInitializer;
import org.hibernate.SessionFactory;

public class Main {
    private static Server server;
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        try {
            sessionFactory = HibernateUtil.getSessionFactory();
            DataInitializer.initialize();
            server = new Server();
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down server...");
                if (server != null) {
                    server.stop();
                }
                if (sessionFactory != null) {
                    HibernateUtil.shutdown();
                }
            }));

        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            System.exit(1);
        }
    }
}
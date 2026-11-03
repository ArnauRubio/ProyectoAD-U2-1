package models;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;

public class ConexionHibernate {

    private static final SessionFactory sessionFactory = construir();

    private static SessionFactory construir() {
        try {
            // Lee META-INF/persistence.xml, unidad de persistencia "default"
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            return emf.unwrap(SessionFactory.class);
        } catch (Exception e) {
            System.err.println("No se pudo conectar con la base de datos: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /** Llamar al salir de la aplicación. */
    public static void cerrar() {
        sessionFactory.close();
    }
}
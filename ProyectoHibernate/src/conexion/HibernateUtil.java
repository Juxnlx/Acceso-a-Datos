package conexion;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase utilitaria para gestionar la SessionFactory de Hibernate.
 * Equivalente a la clase Conexion de JDBC, pero para Hibernate.
 */
public class HibernateUtil {
    
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Crea la SessionFactory desde hibernate.cfg.xml
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            
            System.out.println("SessionFactory creada correctamente.");
            
        } catch (Exception e) {
            System.err.println("Error al crear SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * Obtiene la SessionFactory de Hibernate.
     * 
     * @return SessionFactory configurada
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     * Cierra la SessionFactory cuando la aplicación termina.
     */
    public static void cerrarSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("SessionFactory cerrada.");
        }
    }
}
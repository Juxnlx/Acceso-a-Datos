package conexion;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase de utilidad para gestionar la SessionFactory de Hibernate.
 * Se encarga de crear y proporcionar acceso a la sesión de Hibernate.
 */
public class HibernateUtil {
    
    /**
     * SessionFactory única para toda la aplicación (patrón Singleton)
     */
    private static SessionFactory sessionFactory;
    
    /**
     * Bloque estático que se ejecuta al cargar la clase.
     * Inicializa la SessionFactory desde el archivo hibernate.cfg.xml
     */
    static {
        buildSessionFactory();
    }
    
    /**
     * Construye la SessionFactory
     */
    private static void buildSessionFactory() {
        try {
            // Crear SessionFactory desde el archivo de configuración
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            
            System.out.println("✓ SessionFactory creada exitosamente");
            
        } catch (Exception e) {
            System.err.println("✗ Error al crear SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * Obtiene la instancia de SessionFactory
     * 
     * @return SessionFactory única de la aplicación
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            buildSessionFactory();
        }
        return sessionFactory;
    }
    
    /**
     * Reinicia la SessionFactory.
     * Útil después de eliminar tablas para que Hibernate las vuelva a crear.
     */
    public static void restart() {
        shutdown();
        buildSessionFactory();
        System.out.println("✓ SessionFactory reiniciada correctamente");
    }
    
    /**
     * Cierra la SessionFactory liberando todos los recursos.
     * Debe llamarse al finalizar la aplicación.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("✓ SessionFactory cerrada correctamente");
        }
    }
}
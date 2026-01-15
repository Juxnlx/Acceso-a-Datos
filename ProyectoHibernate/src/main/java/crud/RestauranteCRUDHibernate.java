package crud;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import conexion.HibernateUtil;
import entidades.Factura;
import entidades.Mesa;
import entidades.Pedido;
import entidades.Producto;

/**
 * Clase CRUD para gestionar las operaciones de base de datos usando Hibernate.
 * Incluye métodos para crear tablas, insertar, listar, modificar, borrar y eliminar tablas.
 */
public class RestauranteCRUDHibernate {

    // ==================== CREAR TABLAS ====================
    
	/**
	 * Las tablas se crean automáticamente gracias a hibernate.hbm2ddl.auto=update
	 * Este método reinicia Hibernate para forzar la creación de las tablas.
	 */
	public static void crearTablas() {
	    System.out.println("✓ Las tablas se crean/actualizan automáticamente con Hibernate");
	    System.out.println("  Tablas disponibles: Mesa, Productos, Factura, Pedido");
	    System.out.println();
	    System.out.println("Reiniciando Hibernate y creando tablas...");
	    
	    // Reiniciar Hibernate para que cree las tablas si no existen
	    HibernateUtil.restart();
	    
	    System.out.println("✓ Las tablas han sido creadas correctamente");
	}

    // ==================== INSERTAR ====================
    
    /**
     * Inserta una nueva mesa en la base de datos.
     * 
     * @param numComensales número de comensales de la mesa.
     * @param reserva estado de reserva (1 reservada, 0 libre).
     * @return true si la inserción fue exitosa, false si hubo error.
     */
    public static boolean insertarMesa(int numComensales, int reserva) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Mesa mesa = new Mesa(numComensales, reserva);
            session.persist(mesa);
            
            transaction.commit();
            System.out.println("✓ Mesa insertada correctamente con ID: " + mesa.getIdMesa());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("✗ Error al insertar mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta un nuevo producto en la base de datos.
     * 
     * @param nombre nombre del producto.
     * @param precio precio del producto.
     * @return true si la inserción fue exitosa, false si hubo error.
     */
    public static boolean insertarProducto(String nombre, double precio) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Producto producto = new Producto(nombre, precio);
            session.persist(producto);
            
            transaction.commit();
            System.out.println("✓ Producto insertado correctamente con ID: " + producto.getIdProducto());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("✗ Error al insertar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta una nueva factura en la base de datos.
     * 
     * @param idMesa ID de la mesa asociada.
     * @param tipoPago tipo de pago ("Efectivo", "Tarjeta").
     * @param importe importe total.
     * @return true si la inserción fue exitosa, false si hubo error.
     */
    public static boolean insertarFactura(int idMesa, String tipoPago, double importe) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Primero obtenemos la mesa
            Mesa mesa = session.get(Mesa.class, idMesa);
            if (mesa == null) {
                System.out.println("✗ Error: No existe una mesa con ID " + idMesa);
                System.out.println("  No se puede insertar una factura para una mesa que no existe en la base de datos.");
                return false;
            }
            
            Factura factura = new Factura(mesa, tipoPago, importe);
            session.persist(factura);
            
            transaction.commit();
            System.out.println("✓ Factura insertada correctamente con ID: " + factura.getIdFactura());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("✗ Error al insertar factura: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta un nuevo pedido en la base de datos.
     * 
     * @param idFactura ID de la factura asociada.
     * @param idProducto ID del producto.
     * @param cantidad cantidad pedida.
     * @return true si la inserción fue exitosa, false si hubo error.
     */
    public static boolean insertarPedido(int idFactura, int idProducto, int cantidad) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Obtenemos la factura y el producto
            Factura factura = session.get(Factura.class, idFactura);
            Producto producto = session.get(Producto.class, idProducto);
            
            if (factura == null) {
                System.out.println("✗ Error: No existe una factura con ID " + idFactura);
                System.out.println("  No se puede insertar un pedido para una factura que no existe en la base de datos.");
                return false;
            }
            if (producto == null) {
                System.out.println("✗ Error: No existe un producto con ID " + idProducto);
                System.out.println("  No se puede insertar un pedido para un producto que no existe en la base de datos.");
                return false;
            }
            
            Pedido pedido = new Pedido(factura, producto, cantidad);
            session.persist(pedido);
            
            transaction.commit();
            System.out.println("✓ Pedido insertado correctamente con ID: " + pedido.getIdPedido());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("✗ Error al insertar pedido: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== LISTAR ====================
    
    /**
     * Lista registros de una tabla con opción de filtrado.
     * 
     * @param tabla Nombre de la tabla (Mesa, Producto, Factura, Pedido)
     * @param campo Campo por el que filtrar (null para listar todo)
     * @param valor Valor del filtro (null para listar todo)
     * @return String con los registros encontrados
     */
    public static String listar(String tabla, String campo, String valor) {
        StringBuilder resultado = new StringBuilder();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            switch (tabla.toLowerCase()) {
                case "mesa":
                    if (campo != null && valor != null) {
                        String hql = "FROM Mesa WHERE " + campo + " = :valor";
                        Query<Mesa> queryMesa = session.createQuery(hql, Mesa.class);
                        queryMesa.setParameter("valor", Integer.parseInt(valor));
                        List<Mesa> mesas = queryMesa.list();
                        if (mesas.isEmpty()) {
                            resultado.append("No se encontraron mesas con ese filtro.");
                        } else {
                            mesas.forEach(m -> resultado.append(m).append("\n"));
                        }
                    } else {
                        List<Mesa> mesas = session.createQuery("FROM Mesa", Mesa.class).list();
                        if (mesas.isEmpty()) {
                            resultado.append("No hay mesas registradas.");
                        } else {
                            mesas.forEach(m -> resultado.append(m).append("\n"));
                        }
                    }
                    break;
                    
                case "producto":
                    if (campo != null && valor != null) {
                        String hql = "FROM Producto WHERE " + campo + " LIKE :valor";
                        Query<Producto> queryProd = session.createQuery(hql, Producto.class);
                        queryProd.setParameter("valor", "%" + valor + "%");
                        List<Producto> productos = queryProd.list();
                        if (productos.isEmpty()) {
                            resultado.append("No se encontraron productos con ese filtro.");
                        } else {
                            productos.forEach(p -> resultado.append(p).append("\n"));
                        }
                    } else {
                        List<Producto> productos = session.createQuery("FROM Producto", Producto.class).list();
                        if (productos.isEmpty()) {
                            resultado.append("No hay productos registrados.");
                        } else {
                            productos.forEach(p -> resultado.append(p).append("\n"));
                        }
                    }
                    break;
                    
                case "factura":
                    if (campo != null && valor != null) {
                        String hql = "FROM Factura WHERE " + campo + " = :valor";
                        Query<Factura> queryFact = session.createQuery(hql, Factura.class);
                        queryFact.setParameter("valor", valor);
                        List<Factura> facturas = queryFact.list();
                        if (facturas.isEmpty()) {
                            resultado.append("No se encontraron facturas con ese filtro.");
                        } else {
                            facturas.forEach(f -> resultado.append(f).append("\n"));
                        }
                    } else {
                        List<Factura> facturas = session.createQuery("FROM Factura", Factura.class).list();
                        if (facturas.isEmpty()) {
                            resultado.append("No hay facturas registradas.");
                        } else {
                            facturas.forEach(f -> resultado.append(f).append("\n"));
                        }
                    }
                    break;
                    
                case "pedido":
                    if (campo != null && valor != null) {
                        String hql = "FROM Pedido WHERE " + campo + " = :valor";
                        Query<Pedido> queryPed = session.createQuery(hql, Pedido.class);
                        queryPed.setParameter("valor", Integer.parseInt(valor));
                        List<Pedido> pedidos = queryPed.list();
                        if (pedidos.isEmpty()) {
                            resultado.append("No se encontraron pedidos con ese filtro.");
                        } else {
                            pedidos.forEach(p -> resultado.append(p).append("\n"));
                        }
                    } else {
                        List<Pedido> pedidos = session.createQuery("FROM Pedido", Pedido.class).list();
                        if (pedidos.isEmpty()) {
                            resultado.append("No hay pedidos registrados.");
                        } else {
                            pedidos.forEach(p -> resultado.append(p).append("\n"));
                        }
                    }
                    break;
                    
                default:
                    return "✗ Tabla no válida. Opciones: Mesa, Producto, Factura, Pedido";
            }
            
            return resultado.toString();
            
        } catch (Exception e) {
            return "✗ Error al listar: " + e.getMessage();
        }
    }
    
    // ==================== MODIFICAR ====================
    
    /**
     * Modifica una mesa existente.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idMesa ID de la mesa a modificar
     * @param numComensales Nuevo número de comensales (null para no cambiar)
     * @param reserva Nuevo estado de reserva (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarMesa(Session session, int idMesa, Integer numComensales, Integer reserva) {
        try {
            Mesa mesa = session.get(Mesa.class, idMesa);
            if (mesa == null) {
                System.out.println("✗ No existe una mesa con ID " + idMesa);
                return false;
            }
            
            if (numComensales != null) mesa.setNumComensales(numComensales);
            if (reserva != null) mesa.setReserva(reserva);
            
            session.merge(mesa);
            System.out.println("✓ Mesa modificada correctamente");
            return true;
            
        } catch (Exception e) {
            System.out.println("✗ Error al modificar mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifica un producto existente.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idProducto ID del producto a modificar
     * @param nombre Nuevo nombre (null para no cambiar)
     * @param precio Nuevo precio (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarProducto(Session session, int idProducto, String nombre, Double precio) {
        try {
            Producto producto = session.get(Producto.class, idProducto);
            if (producto == null) {
                System.out.println("✗ No existe un producto con ID " + idProducto);
                return false;
            }
            
            if (nombre != null) producto.setNombre(nombre);
            if (precio != null) producto.setPrecio(precio);
            
            session.merge(producto);
            System.out.println("✓ Producto modificado correctamente");
            return true;
            
        } catch (Exception e) {
            System.out.println("✗ Error al modificar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifica una factura existente.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idFactura ID de la factura a modificar
     * @param tipoPago Nuevo tipo de pago (null para no cambiar)
     * @param importe Nuevo importe (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarFactura(Session session, int idFactura, String tipoPago, Double importe) {
        try {
            Factura factura = session.get(Factura.class, idFactura);
            if (factura == null) {
                System.out.println("✗ No existe una factura con ID " + idFactura);
                return false;
            }
            
            if (tipoPago != null) factura.setTipoPago(tipoPago);
            if (importe != null) factura.setImporte(importe);
            
            session.merge(factura);
            System.out.println("✓ Factura modificada correctamente");
            return true;
            
        } catch (Exception e) {
            System.out.println("✗ Error al modificar factura: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifica un pedido existente.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idPedido ID del pedido a modificar
     * @param cantidad Nueva cantidad (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarPedido(Session session, int idPedido, Integer cantidad) {
        try {
            Pedido pedido = session.get(Pedido.class, idPedido);
            if (pedido == null) {
                System.out.println("✗ No existe un pedido con ID " + idPedido);
                return false;
            }
            
            if (cantidad != null) pedido.setCantidad(cantidad);
            
            session.merge(pedido);
            System.out.println("✓ Pedido modificado correctamente");
            return true;
            
        } catch (Exception e) {
            System.out.println("✗ Error al modificar pedido: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== BORRAR ====================
    
    /**
     * Borra una mesa por ID.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idMesa ID de la mesa a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarMesa(Session session, int idMesa) {
        try {
            Mesa mesa = session.get(Mesa.class, idMesa);
            if (mesa == null) {
                System.out.println("✗ No existe una mesa con ID " + idMesa);
                return false;
            }
            session.remove(mesa);
            System.out.println("✓ Mesa borrada correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar mesa: " + e.getMessage());
            System.out.println("  Puede que tenga facturas asociadas. Elimínelas primero.");
            return false;
        }
    }
    
    /**
     * Borra un producto por ID.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idProducto ID del producto a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarProducto(Session session, int idProducto) {
        try {
            Producto producto = session.get(Producto.class, idProducto);
            if (producto == null) {
                System.out.println("✗ No existe un producto con ID " + idProducto);
                return false;
            }
            session.remove(producto);
            System.out.println("✓ Producto borrado correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar producto: " + e.getMessage());
            System.out.println("  Puede que tenga pedidos asociados. Elimínelos primero.");
            return false;
        }
    }
    
    /**
     * Borra una factura por ID.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idFactura ID de la factura a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarFactura(Session session, int idFactura) {
        try {
            Factura factura = session.get(Factura.class, idFactura);
            if (factura == null) {
                System.out.println("✗ No existe una factura con ID " + idFactura);
                return false;
            }
            session.remove(factura);
            System.out.println("✓ Factura borrada correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar factura: " + e.getMessage());
            System.out.println("  Puede que tenga pedidos asociados. Elimínelos primero.");
            return false;
        }
    }
    
    /**
     * Borra un pedido por ID.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param idPedido ID del pedido a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarPedido(Session session, int idPedido) {
        try {
            Pedido pedido = session.get(Pedido.class, idPedido);
            if (pedido == null) {
                System.out.println("✗ No existe un pedido con ID " + idPedido);
                return false;
            }
            session.remove(pedido);
            System.out.println("✓ Pedido borrado correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar pedido: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra todas las mesas.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodasMesas(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Mesa");
            int deletedCount = query.executeUpdate();
            System.out.println("✓ Se borraron " + deletedCount + " mesas");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar todas las mesas: " + e.getMessage());
            System.out.println("  Puede que tengan facturas asociadas. Elimínelas primero.");
            return false;
        }
    }
    
    /**
     * Borra todos los productos.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodosProductos(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Producto");
            int deletedCount = query.executeUpdate();
            System.out.println("✓ Se borraron " + deletedCount + " productos");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar todos los productos: " + e.getMessage());
            System.out.println("  Puede que tengan pedidos asociados. Elimínelos primero.");
            return false;
        }
    }
    
    /**
     * Borra todas las facturas.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodasFacturas(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Factura");
            int deletedCount = query.executeUpdate();
            System.out.println("✓ Se borraron " + deletedCount + " facturas");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar todas las facturas: " + e.getMessage());
            System.out.println("  Puede que tengan pedidos asociados. Elimínelos primero.");
            return false;
        }
    }
    
    /**
     * Borra todos los pedidos.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodosPedidos(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Pedido");
            int deletedCount = query.executeUpdate();
            System.out.println("✓ Se borraron " + deletedCount + " pedidos");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error al borrar todos los pedidos: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== ELIMINAR TABLAS (DROP) ====================
    
    /**
     * Elimina una tabla específica (DROP TABLE).
     * NOTA: Esto usa SQL nativo y debe hacerse con cuidado.
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @param tabla Nombre de la tabla a eliminar
     * @return true si se eliminó correctamente
     */
    public static boolean eliminarTabla(Session session, String tabla) {
        try {
            // Desactivar temporalmente las comprobaciones de claves foráneas
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0", Object.class).executeUpdate();
            
            String sql = "DROP TABLE IF EXISTS " + tabla;
            session.createNativeQuery(sql, Object.class).executeUpdate();
            
            // Reactivar las comprobaciones de claves foráneas
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1", Object.class).executeUpdate();
            
            System.out.println("✓ Tabla " + tabla + " eliminada correctamente");
            return true;
            
        } catch (Exception e) {
            System.out.println("✗ Error al eliminar tabla " + tabla + ": " + e.getMessage());
            System.out.println("  Verifica que el nombre de la tabla sea correcto");
            System.out.println("  Tablas disponibles: Mesa, Productos, Factura, Pedido");
            return false;
        }
    }
    
    /**
     * Elimina todas las tablas en el orden correcto (DROP TABLE).
     * Orden: Pedido → Factura → Productos → Mesa
     * 
     * @param session Sesión de Hibernate activa con transacción iniciada
     * @return true si se eliminaron correctamente
     */
    public static boolean eliminarTodasTablas(Session session) {
        try {
            // Primero desactivar las comprobaciones de claves foráneas
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0", Object.class).executeUpdate();
            
            // Orden inverso por dependencias
            try {
                session.createNativeQuery("DROP TABLE IF EXISTS Pedido", Object.class).executeUpdate();
                System.out.println("✓ Tabla Pedido eliminada");
            } catch (Exception e) {
                System.out.println("⚠ Tabla Pedido no existe o ya fue eliminada");
            }
            
            try {
                session.createNativeQuery("DROP TABLE IF EXISTS Factura", Object.class).executeUpdate();
                System.out.println("✓ Tabla Factura eliminada");
            } catch (Exception e) {
                System.out.println("⚠ Tabla Factura no existe o ya fue eliminada");
            }
            
            try {
                session.createNativeQuery("DROP TABLE IF EXISTS Productos", Object.class).executeUpdate();
                System.out.println("✓ Tabla Productos eliminada");
            } catch (Exception e) {
                System.out.println("⚠ Tabla Productos no existe o ya fue eliminada");
            }
            
            try {
                session.createNativeQuery("DROP TABLE IF EXISTS Mesa", Object.class).executeUpdate();
                System.out.println("✓ Tabla Mesa eliminada");
            } catch (Exception e) {
                System.out.println("⚠ Tabla Mesa no existe o ya fue eliminada");
            }
            
            // Reactivar las comprobaciones de claves foráneas
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1", Object.class).executeUpdate();
            
            System.out.println("✓ Proceso de eliminación completado");
            return true;
            
        } catch (Exception e) {
            System.out.println("✗ Error al eliminar tablas: " + e.getMessage());
            return false;
        }
    }
}
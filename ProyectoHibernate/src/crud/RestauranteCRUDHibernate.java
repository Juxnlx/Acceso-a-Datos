package crud;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import conexion.HibernateUtil;
import entidades.*;

import java.util.List;

public class RestauranteCRUDHibernate {

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
            session.save(mesa);
            
            transaction.commit();
            System.out.println("Mesa insertada correctamente con ID: " + mesa.getIdMesa());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error al insertar mesa: " + e.getMessage());
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
            session.save(producto);
            
            transaction.commit();
            System.out.println("Producto insertado correctamente con ID: " + producto.getIdProducto());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error al insertar producto: " + e.getMessage());
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
                System.out.println("Error: No existe una mesa con ID " + idMesa);
                return false;
            }
            
            Factura factura = new Factura(mesa, tipoPago, importe);
            session.save(factura);
            
            transaction.commit();
            System.out.println("Factura insertada correctamente con ID: " + factura.getIdFactura());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error al insertar factura: " + e.getMessage());
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
                System.out.println("Error: No existe una factura con ID " + idFactura);
                return false;
            }
            if (producto == null) {
                System.out.println("Error: No existe un producto con ID " + idProducto);
                return false;
            }
            
            Pedido pedido = new Pedido(factura, producto, cantidad);
            session.save(pedido);
            
            transaction.commit();
            System.out.println("Pedido insertado correctamente con ID: " + pedido.getIdPedido());
            return true;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error al insertar pedido: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== LISTAR ====================
    
    /**
     * Lista todas las mesas de la base de datos.
     * 
     * @return String con los registros o mensaje si no hay resultados.
     */
    public static String listarMesas() {
        StringBuilder resultado = new StringBuilder();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            List<Mesa> mesas = session.createQuery("FROM Mesa", Mesa.class).list();
            
            if (mesas.isEmpty()) {
                resultado.append("No hay mesas registradas.");
            } else {
                for (Mesa mesa : mesas) {
                    resultado.append(mesa.toString()).append("\n");
                }
            }
            
        } catch (Exception e) {
            resultado.append("Error al listar mesas: ").append(e.getMessage());
        }
        return resultado.toString();
    }
    
    /**
     * Lista todos los productos de la base de datos.
     * 
     * @return String con los registros o mensaje si no hay resultados.
     */
    public static String listarProductos() {
        StringBuilder resultado = new StringBuilder();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            List<Producto> productos = session.createQuery("FROM Producto", Producto.class).list();
            
            if (productos.isEmpty()) {
                resultado.append("No hay productos registrados.");
            } else {
                for (Producto producto : productos) {
                    resultado.append(producto.toString()).append("\n");
                }
            }
            
        } catch (Exception e) {
            resultado.append("Error al listar productos: ").append(e.getMessage());
        }
        return resultado.toString();
    }
    
    /**
     * Lista todas las facturas de la base de datos.
     * 
     * @return String con los registros o mensaje si no hay resultados.
     */
    public static String listarFacturas() {
        StringBuilder resultado = new StringBuilder();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            List<Factura> facturas = session.createQuery("FROM Factura", Factura.class).list();
            
            if (facturas.isEmpty()) {
                resultado.append("No hay facturas registradas.");
            } else {
                for (Factura factura : facturas) {
                    resultado.append(factura.toString()).append("\n");
                }
            }
            
        } catch (Exception e) {
            resultado.append("Error al listar facturas: ").append(e.getMessage());
        }
        return resultado.toString();
    }
    
    /**
     * Lista todos los pedidos de la base de datos.
     * 
     * @return String con los registros o mensaje si no hay resultados.
     */
    public static String listarPedidos() {
        StringBuilder resultado = new StringBuilder();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            List<Pedido> pedidos = session.createQuery("FROM Pedido", Pedido.class).list();
            
            if (pedidos.isEmpty()) {
                resultado.append("No hay pedidos registrados.");
            } else {
                for (Pedido pedido : pedidos) {
                    resultado.append(pedido.toString()).append("\n");
                }
            }
            
        } catch (Exception e) {
            resultado.append("Error al listar pedidos: ").append(e.getMessage());
        }
        return resultado.toString();
    }
    
    // ==================== MODIFICAR ====================
    
    /**
     * Modifica una mesa existente.
     * 
     * @param session Sesión de Hibernate activa
     * @param idMesa ID de la mesa a modificar
     * @param numComensales Nuevo número de comensales (null para no cambiar)
     * @param reserva Nuevo estado de reserva (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarMesa(Session session, int idMesa, Integer numComensales, Integer reserva) {
        try {
            Mesa mesa = session.get(Mesa.class, idMesa);
            if (mesa == null) {
                System.out.println("No existe una mesa con ID " + idMesa);
                return false;
            }
            
            if (numComensales != null) mesa.setNumComensales(numComensales);
            if (reserva != null) mesa.setReserva(reserva);
            
            session.update(mesa);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al modificar mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifica un producto existente.
     * 
     * @param session Sesión de Hibernate activa
     * @param idProducto ID del producto a modificar
     * @param nombre Nuevo nombre (null para no cambiar)
     * @param precio Nuevo precio (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarProducto(Session session, int idProducto, String nombre, Double precio) {
        try {
            Producto producto = session.get(Producto.class, idProducto);
            if (producto == null) {
                System.out.println("No existe un producto con ID " + idProducto);
                return false;
            }
            
            if (nombre != null) producto.setNombre(nombre);
            if (precio != null) producto.setPrecio(precio);
            
            session.update(producto);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al modificar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifica una factura existente.
     * 
     * @param session Sesión de Hibernate activa
     * @param idFactura ID de la factura a modificar
     * @param tipoPago Nuevo tipo de pago (null para no cambiar)
     * @param importe Nuevo importe (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarFactura(Session session, int idFactura, String tipoPago, Double importe) {
        try {
            Factura factura = session.get(Factura.class, idFactura);
            if (factura == null) {
                System.out.println("No existe una factura con ID " + idFactura);
                return false;
            }
            
            if (tipoPago != null) factura.setTipoPago(tipoPago);
            if (importe != null) factura.setImporte(importe);
            
            session.update(factura);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al modificar factura: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifica un pedido existente.
     * 
     * @param session Sesión de Hibernate activa
     * @param idPedido ID del pedido a modificar
     * @param cantidad Nueva cantidad (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public static boolean modificarPedido(Session session, int idPedido, Integer cantidad) {
        try {
            Pedido pedido = session.get(Pedido.class, idPedido);
            if (pedido == null) {
                System.out.println("No existe un pedido con ID " + idPedido);
                return false;
            }
            
            if (cantidad != null) pedido.setCantidad(cantidad);
            
            session.update(pedido);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al modificar pedido: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== BORRAR ====================
    
    /**
     * Borra una mesa por ID.
     * 
     * @param session Sesión de Hibernate activa
     * @param idMesa ID de la mesa a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarMesa(Session session, int idMesa) {
        try {
            Mesa mesa = session.get(Mesa.class, idMesa);
            if (mesa == null) {
                System.out.println("No existe una mesa con ID " + idMesa);
                return false;
            }
            session.delete(mesa);
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra un producto por ID.
     * 
     * @param session Sesión de Hibernate activa
     * @param idProducto ID del producto a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarProducto(Session session, int idProducto) {
        try {
            Producto producto = session.get(Producto.class, idProducto);
            if (producto == null) {
                System.out.println("No existe un producto con ID " + idProducto);
                return false;
            }
            session.delete(producto);
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra una factura por ID.
     * 
     * @param session Sesión de Hibernate activa
     * @param idFactura ID de la factura a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarFactura(Session session, int idFactura) {
        try {
            Factura factura = session.get(Factura.class, idFactura);
            if (factura == null) {
                System.out.println("No existe una factura con ID " + idFactura);
                return false;
            }
            session.delete(factura);
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar factura: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra un pedido por ID.
     * 
     * @param session Sesión de Hibernate activa
     * @param idPedido ID del pedido a borrar
     * @return true si se borró correctamente
     */
    public static boolean borrarPedido(Session session, int idPedido) {
        try {
            Pedido pedido = session.get(Pedido.class, idPedido);
            if (pedido == null) {
                System.out.println("No existe un pedido con ID " + idPedido);
                return false;
            }
            session.delete(pedido);
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar pedido: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra todas las mesas.
     * 
     * @param session Sesión de Hibernate activa
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodasMesas(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Mesa");
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar todas las mesas: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra todos los productos.
     * 
     * @param session Sesión de Hibernate activa
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodosProductos(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Producto");
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar todos los productos: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra todas las facturas.
     * 
     * @param session Sesión de Hibernate activa
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodasFacturas(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Factura");
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar todas las facturas: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Borra todos los pedidos.
     * 
     * @param session Sesión de Hibernate activa
     * @return true si se borraron correctamente
     */
    public static boolean borrarTodosPedidos(Session session) {
        try {
            Query query = session.createQuery("DELETE FROM Pedido");
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar todos los pedidos: " + e.getMessage());
            return false;
        }
    }
}
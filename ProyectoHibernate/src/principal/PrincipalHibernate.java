package principal;

import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import conexion.HibernateUtil;
import crud.RestauranteCRUDHibernate;

public class PrincipalHibernate {

    /** Scanner para leer datos desde consola */
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Opción seleccionada por el usuario
        int opc;

        do {
            mostrarMenu(); // Mostramos el menú principal
            System.out.print("Introduce una opción --> ");
            opc = sc.nextInt();
            sc.nextLine(); // Limpiamos buffer

            switch (opc) {
                case 1 -> gestionInsertarMesa();
                case 2 -> gestionInsertarProducto();
                case 3 -> gestionInsertarFactura();
                case 4 -> gestionInsertarPedido();
                case 5 -> gestionListar();
                case 6 -> modificar();
                case 7 -> borrarDesdeMain();
                case 0 -> {
                    System.out.println("Saliendo del programa...");
                    HibernateUtil.cerrarSessionFactory();
                }
                default -> System.out.println("Opción no válida.");
            }

        } while (opc != 0);

        sc.close(); // Cerramos Scanner
    }

    /**
     * Muestra el menú principal con todas las opciones disponibles
     */
    public static void mostrarMenu() {
        System.out.println("\n----------MENÚ HIBERNATE----------");
        System.out.println("1. Insertar Mesa");
        System.out.println("2. Insertar Producto");
        System.out.println("3. Insertar Factura");
        System.out.println("4. Insertar Pedido");
        System.out.println("5. Listar");
        System.out.println("6. Modificar");
        System.out.println("7. Borrar");
        System.out.println("0. Salir");
    }

    /**
     * Gestiona la inserción de una nueva mesa.
     * Solicita número de comensales y si tiene reserva.
     */
    public static void gestionInsertarMesa() {
        System.out.println("\n----- INSERTAR MESA -----");
        System.out.print("Número de comensales: ");
        int numComensales = sc.nextInt();
        sc.nextLine();
        System.out.print("¿Tiene reserva? (1 = Sí, 0 = No): ");
        int reserva = sc.nextInt();
        sc.nextLine();

        if (RestauranteCRUDHibernate.insertarMesa(numComensales, reserva)) {
            System.out.println("Mesa insertada correctamente.");
        } else {
            System.out.println("Error al insertar mesa.");
        }
    }

    /**
     * Gestiona la inserción de un nuevo producto.
     * Solicita nombre y precio del producto.
     */
    public static void gestionInsertarProducto() {
        System.out.println("\n----- INSERTAR PRODUCTO -----");
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine();
        System.out.print("Precio del producto: ");
        double precio = sc.nextDouble();
        sc.nextLine();

        if (RestauranteCRUDHibernate.insertarProducto(nombre, precio)) {
            System.out.println("Producto insertado correctamente.");
        } else {
            System.out.println("Error al insertar producto.");
        }
    }

    /**
     * Gestiona la inserción de una nueva factura.
     * Solicita mesa, tipo de pago e importe.
     */
    public static void gestionInsertarFactura() {
        System.out.println("\n----- INSERTAR FACTURA -----");
        System.out.println("Mesas disponibles:");
        System.out.println(RestauranteCRUDHibernate.listarMesas());

        System.out.print("ID de mesa: ");
        int idMesa = sc.nextInt();
        sc.nextLine();
        System.out.print("Tipo de pago (Efectivo/Tarjeta): ");
        String tipoPago = sc.nextLine();
        System.out.print("Importe de factura: ");
        double importe = sc.nextDouble();
        sc.nextLine();

        if (RestauranteCRUDHibernate.insertarFactura(idMesa, tipoPago, importe)) {
            System.out.println("Factura insertada correctamente.");
        } else {
            System.out.println("Error al insertar factura. Verifica que la mesa existe.");
        }
    }

    /**
     * Gestiona la inserción de un nuevo pedido.
     * Solicita ID de factura, ID de producto y cantidad.
     */
    public static void gestionInsertarPedido() {
        System.out.println("\n----- INSERTAR PEDIDO -----");
        System.out.println("Facturas disponibles:");
        System.out.println(RestauranteCRUDHibernate.listarFacturas());
        System.out.print("ID de Factura: ");
        int idFactura = sc.nextInt();
        sc.nextLine();
        System.out.println("Productos disponibles:");
        System.out.println(RestauranteCRUDHibernate.listarProductos());
        System.out.print("ID de Producto: ");
        int idProducto = sc.nextInt();
        sc.nextLine();
        System.out.print("Cantidad: ");
        int cantidad = sc.nextInt();
        sc.nextLine();

        if (RestauranteCRUDHibernate.insertarPedido(idFactura, idProducto, cantidad)) {
            System.out.println("Pedido insertado correctamente.");
        } else {
            System.out.println("Error al insertar pedido. Verifica que la factura y producto existen.");
        }
    }

    /**
     * Gestiona la visualización de los registros de la base de datos.
     */
    public static void gestionListar() {
        System.out.println("\n¿Qué tabla quieres listar?");
        System.out.println("1. Mesa");
        System.out.println("2. Producto");
        System.out.println("3. Factura");
        System.out.println("4. Pedido");
        System.out.print("Opción: ");
        int opc = sc.nextInt();
        sc.nextLine();

        switch (opc) {
            case 1 -> {
                System.out.println("\n----- MESAS -----");
                System.out.println(RestauranteCRUDHibernate.listarMesas());
            }
            case 2 -> {
                System.out.println("\n----- PRODUCTOS -----");
                System.out.println(RestauranteCRUDHibernate.listarProductos());
            }
            case 3 -> {
                System.out.println("\n----- FACTURAS -----");
                System.out.println(RestauranteCRUDHibernate.listarFacturas());
            }
            case 4 -> {
                System.out.println("\n----- PEDIDOS -----");
                System.out.println(RestauranteCRUDHibernate.listarPedidos());
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    /**
     * Gestiona la modificación de un registro.
     */
    public static void modificar() {
        System.out.println("\n----- MODIFICAR -----");
        System.out.println("¿Qué tabla quieres modificar?");
        System.out.println("1. Mesa");
        System.out.println("2. Producto");
        System.out.println("3. Factura");
        System.out.println("4. Pedido");
        System.out.print("Opción: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean modificado = false;

            switch (opcion) {
                case 1 -> modificado = modificarMesa(session);
                case 2 -> modificado = modificarProducto(session);
                case 3 -> modificado = modificarFactura(session);
                case 4 -> modificado = modificarPedido(session);
                default -> System.out.println("Opción no válida.");
            }

            if (!modificado) {
                System.out.println("No se modificó ningún registro.");
                transaction.rollback();
                return;
            }

            System.out.println("\n¿Confirmar cambios? (s/n): ");
            if (sc.nextLine().equalsIgnoreCase("s")) {
                transaction.commit();
                System.out.println("Cambios guardados.");
            } else {
                transaction.rollback();
                System.out.println("Cambios deshechos.");
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error al modificar: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    private static boolean modificarMesa(Session session) {
        System.out.println(RestauranteCRUDHibernate.listarMesas());
        System.out.print("ID de la mesa a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nuevo número de comensales (enter para no cambiar): ");
        String comensalesStr = sc.nextLine();
        Integer comensales = comensalesStr.isEmpty() ? null : Integer.parseInt(comensalesStr);
        System.out.print("Nueva reserva (1=Sí, 0=No, enter para no cambiar): ");
        String reservaStr = sc.nextLine();
        Integer reserva = reservaStr.isEmpty() ? null : Integer.parseInt(reservaStr);
        
        return RestauranteCRUDHibernate.modificarMesa(session, id, comensales, reserva);
    }

    private static boolean modificarProducto(Session session) {
        System.out.println(RestauranteCRUDHibernate.listarProductos());
        System.out.print("ID del producto a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nuevo nombre (enter para no cambiar): ");
        String nombre = sc.nextLine();
        nombre = nombre.isEmpty() ? null : nombre;
        System.out.print("Nuevo precio (0 para no cambiar): ");
        double precioInput = sc.nextDouble();
        sc.nextLine();
        Double precio = precioInput == 0 ? null : precioInput;
        
        return RestauranteCRUDHibernate.modificarProducto(session, id, nombre, precio);
    }

    private static boolean modificarFactura(Session session) {
        System.out.println(RestauranteCRUDHibernate.listarFacturas());
        System.out.print("ID de la factura a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nuevo tipo de pago (enter para no cambiar): ");
        String tipoPago = sc.nextLine();
        tipoPago = tipoPago.isEmpty() ? null : tipoPago;
        System.out.print("Nuevo importe (0 para no cambiar): ");
        double importeInput = sc.nextDouble();
        sc.nextLine();
        Double importe = importeInput == 0 ? null : importeInput;
        
        return RestauranteCRUDHibernate.modificarFactura(session, id, tipoPago, importe);
    }

    private static boolean modificarPedido(Session session) {
        System.out.println(RestauranteCRUDHibernate.listarPedidos());
        System.out.print("ID del pedido a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nueva cantidad (0 para no cambiar): ");
        int cantidadInput = sc.nextInt();
        sc.nextLine();
        Integer cantidad = cantidadInput == 0 ? null : cantidadInput;
        
        return RestauranteCRUDHibernate.modificarPedido(session, id, cantidad);
    }

    /**
     * Gestiona el borrado de registros.
     */
    public static void borrarDesdeMain() {
        System.out.println("\n----- BORRAR -----");
        System.out.println("¿Qué tabla quieres borrar?");
        System.out.println("1. Mesa");
        System.out.println("2. Producto");
        System.out.println("3. Factura");
        System.out.println("4. Pedido");
        System.out.print("Opción: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        System.out.print("¿Borrar toda la tabla? (s/n): ");
        boolean borrarTodo = sc.nextLine().equalsIgnoreCase("s");

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean exito = false;

            if (borrarTodo) {
                switch (opcion) {
                    case 1 -> exito = RestauranteCRUDHibernate.borrarTodasMesas(session);
                    case 2 -> exito = RestauranteCRUDHibernate.borrarTodosProductos(session);
                    case 3 -> exito = RestauranteCRUDHibernate.borrarTodasFacturas(session);
                    case 4 -> exito = RestauranteCRUDHibernate.borrarTodosPedidos(session);
                    default -> System.out.println("Opción no válida.");
                }
            } else {
                System.out.print("ID del registro a borrar: ");
                int id = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1 -> exito = RestauranteCRUDHibernate.borrarMesa(session, id);
                    case 2 -> exito = RestauranteCRUDHibernate.borrarProducto(session, id);
                    case 3 -> exito = RestauranteCRUDHibernate.borrarFactura(session, id);
                    case 4 -> exito = RestauranteCRUDHibernate.borrarPedido(session, id);
                    default -> System.out.println("Opción no válida.");
                }
            }

            if (!exito) {
                System.out.println("No se pudo borrar. Revisa las claves foráneas.");
                transaction.rollback();
                return;
            }

            System.out.println("\n¿Confirmar cambios? (s/n): ");
            if (sc.nextLine().equalsIgnoreCase("s")) {
                transaction.commit();
                System.out.println("Cambios guardados.");
            } else {
                transaction.rollback();
                System.out.println("Cambios deshechos.");
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error al borrar: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }
}

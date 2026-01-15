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
                case 1 -> gestionCrearTablas();
                case 2 -> gestionInsertarMesa();
                case 3 -> gestionInsertarProducto();
                case 4 -> gestionInsertarFactura();
                case 5 -> gestionInsertarPedido();
                case 6 -> gestionListar();
                case 7 -> gestionModificar();
                case 8 -> gestionBorrar();
                case 9 -> gestionEliminarTablas();
                case 0 -> {
                    System.out.println("Saliendo del programa...");
                    HibernateUtil.shutdown();
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
        System.out.println("\n========== MENÚ HIBERNATE ==========");
        System.out.println("1. Crear Tablas");
        System.out.println("2. Insertar Mesa");
        System.out.println("3. Insertar Producto");
        System.out.println("4. Insertar Factura");
        System.out.println("5. Insertar Pedido");
        System.out.println("6. Listar");
        System.out.println("7. Modificar");
        System.out.println("8. Borrar");
        System.out.println("9. Eliminar Tabla (DROP)");
        System.out.println("0. Salir");
        System.out.println("====================================");
    }

    /**
     * Gestiona la creación de tablas.
     * Con Hibernate, las tablas se crean automáticamente.
     */
    public static void gestionCrearTablas() {
        System.out.println("\n----- CREAR TABLAS -----");
        RestauranteCRUDHibernate.crearTablas();
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

        RestauranteCRUDHibernate.insertarMesa(numComensales, reserva);
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

        RestauranteCRUDHibernate.insertarProducto(nombre, precio);
    }

    /**
     * Gestiona la inserción de una nueva factura.
     * Solicita mesa, tipo de pago e importe.
     */
    public static void gestionInsertarFactura() {
        System.out.println("\n----- INSERTAR FACTURA -----");
        System.out.println("Mesas disponibles:");
        System.out.println(RestauranteCRUDHibernate.listar("Mesa", null, null));

        System.out.print("ID de mesa: ");
        int idMesa = sc.nextInt();
        sc.nextLine();
        System.out.print("Tipo de pago (Efectivo/Tarjeta): ");
        String tipoPago = sc.nextLine();
        System.out.print("Importe de factura: ");
        double importe = sc.nextDouble();
        sc.nextLine();

        RestauranteCRUDHibernate.insertarFactura(idMesa, tipoPago, importe);
    }

    /**
     * Gestiona la inserción de un nuevo pedido.
     * Solicita ID de factura, ID de producto y cantidad.
     */
    public static void gestionInsertarPedido() {
        System.out.println("\n----- INSERTAR PEDIDO -----");
        System.out.println("Facturas disponibles:");
        System.out.println(RestauranteCRUDHibernate.listar("Factura", null, null));
        System.out.print("ID de Factura: ");
        int idFactura = sc.nextInt();
        sc.nextLine();
        
        System.out.println("\nProductos disponibles:");
        System.out.println(RestauranteCRUDHibernate.listar("Producto", null, null));
        System.out.print("ID de Producto: ");
        int idProducto = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Cantidad: ");
        int cantidad = sc.nextInt();
        sc.nextLine();

        RestauranteCRUDHibernate.insertarPedido(idFactura, idProducto, cantidad);
    }

    /**
     * Gestiona la visualización de los registros de la base de datos.
     * Permite filtrar por un campo opcional.
     */
    public static void gestionListar() {
        System.out.println("\n----- LISTAR -----");
        System.out.println("¿Qué tabla quieres listar?");
        System.out.println("1. Mesa");
        System.out.println("2. Producto");
        System.out.println("3. Factura");
        System.out.println("4. Pedido");
        System.out.print("Opción: ");
        int opc = sc.nextInt();
        sc.nextLine();

        String tabla = switch (opc) {
            case 1 -> "Mesa";
            case 2 -> "Producto";
            case 3 -> "Factura";
            case 4 -> "Pedido";
            default -> null;
        };

        if (tabla == null) {
            System.out.println("Opción no válida.");
            return;
        }

        // Preguntar si quiere filtrar
        System.out.print("¿Quieres filtrar por algún campo? (s/n): ");
        boolean filtrar = sc.nextLine().equalsIgnoreCase("s");
        
        String campo = null, valor = null;
        
        if (filtrar) {
            System.out.print("Campo por el que filtrar: ");
            campo = sc.nextLine();
            System.out.print("Valor del filtro: ");
            valor = sc.nextLine();
        }

        System.out.println("\n----- " + tabla.toUpperCase() + " -----");
        System.out.println(RestauranteCRUDHibernate.listar(tabla, campo, valor));
    }

    /**
     * Gestiona la modificación de un registro.
     * Soporta transacciones con commit/rollback.
     */
    public static void gestionModificar() {
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
                System.out.println("✗ No se modificó ningún registro.");
                transaction.rollback();
                return;
            }

            // Mostrar los datos después de modificar
            System.out.println("\n--- DATOS DESPUÉS DE LA MODIFICACIÓN ---");
            String tabla = switch (opcion) {
                case 1 -> "Mesa";
                case 2 -> "Producto";
                case 3 -> "Factura";
                case 4 -> "Pedido";
                default -> "";
            };
            System.out.println(RestauranteCRUDHibernate.listar(tabla, null, null));

            System.out.print("\n¿Confirmar cambios? (s/n): ");
            if (sc.nextLine().equalsIgnoreCase("s")) {
                transaction.commit();
                System.out.println("✓ Cambios guardados.");
            } else {
                transaction.rollback();
                System.out.println("✗ Cambios deshechos.");
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("✗ Error al modificar: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Modifica una mesa específica
     */
    private static boolean modificarMesa(Session session) {
        System.out.println("\nMesas disponibles:");
        System.out.println(RestauranteCRUDHibernate.listar("Mesa", null, null));
        
        System.out.print("ID de la mesa a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Nuevo número de comensales (Enter para no cambiar): ");
        String comensalesStr = sc.nextLine();
        Integer comensales = comensalesStr.isEmpty() ? null : Integer.parseInt(comensalesStr);
        
        System.out.print("Nueva reserva (1=Sí, 0=No, Enter para no cambiar): ");
        String reservaStr = sc.nextLine();
        Integer reserva = reservaStr.isEmpty() ? null : Integer.parseInt(reservaStr);
        
        return RestauranteCRUDHibernate.modificarMesa(session, id, comensales, reserva);
    }

    /**
     * Modifica un producto específico
     */
    private static boolean modificarProducto(Session session) {
        System.out.println("\nProductos disponibles:");
        System.out.println(RestauranteCRUDHibernate.listar("Producto", null, null));
        
        System.out.print("ID del producto a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Nuevo nombre (Enter para no cambiar): ");
        String nombre = sc.nextLine();
        nombre = nombre.isEmpty() ? null : nombre;
        
        System.out.print("Nuevo precio (Enter para no cambiar): ");
        String precioStr = sc.nextLine();
        Double precio = precioStr.isEmpty() ? null : Double.parseDouble(precioStr);
        
        return RestauranteCRUDHibernate.modificarProducto(session, id, nombre, precio);
    }

    /**
     * Modifica una factura específica
     */
    private static boolean modificarFactura(Session session) {
        System.out.println("\nFacturas disponibles:");
        System.out.println(RestauranteCRUDHibernate.listar("Factura", null, null));
        
        System.out.print("ID de la factura a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Nuevo tipo de pago (Enter para no cambiar): ");
        String tipoPago = sc.nextLine();
        tipoPago = tipoPago.isEmpty() ? null : tipoPago;
        
        System.out.print("Nuevo importe (Enter para no cambiar): ");
        String importeStr = sc.nextLine();
        Double importe = importeStr.isEmpty() ? null : Double.parseDouble(importeStr);
        
        return RestauranteCRUDHibernate.modificarFactura(session, id, tipoPago, importe);
    }

    /**
     * Modifica un pedido específico
     */
    private static boolean modificarPedido(Session session) {
        System.out.println("\nPedidos disponibles:");
        System.out.println(RestauranteCRUDHibernate.listar("Pedido", null, null));
        
        System.out.print("ID del pedido a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Nueva cantidad (Enter para no cambiar): ");
        String cantidadStr = sc.nextLine();
        Integer cantidad = cantidadStr.isEmpty() ? null : Integer.parseInt(cantidadStr);
        
        return RestauranteCRUDHibernate.modificarPedido(session, id, cantidad);
    }

    /**
     * Gestiona el borrado de registros.
     * Puede borrar un registro específico o todos los de una tabla.
     */
    public static void gestionBorrar() {
        System.out.println("\n----- BORRAR -----");
        System.out.println("¿Qué tabla quieres gestionar?");
        System.out.println("1. Mesa");
        System.out.println("2. Producto");
        System.out.println("3. Factura");
        System.out.println("4. Pedido");
        System.out.print("Opción: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        String tabla = switch (opcion) {
            case 1 -> "Mesa";
            case 2 -> "Producto";
            case 3 -> "Factura";
            case 4 -> "Pedido";
            default -> null;
        };

        if (tabla == null) {
            System.out.println("Opción no válida.");
            return;
        }

        System.out.print("¿Borrar toda la tabla? (s/n): ");
        boolean borrarTodo = sc.nextLine().equalsIgnoreCase("s");

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean exito = false;

            if (borrarTodo) {
                // Borrar todos los registros
                switch (opcion) {
                    case 1 -> exito = RestauranteCRUDHibernate.borrarTodasMesas(session);
                    case 2 -> exito = RestauranteCRUDHibernate.borrarTodosProductos(session);
                    case 3 -> exito = RestauranteCRUDHibernate.borrarTodasFacturas(session);
                    case 4 -> exito = RestauranteCRUDHibernate.borrarTodosPedidos(session);
                }
            } else {
                // Borrar un registro específico
                System.out.println("\nRegistros disponibles:");
                System.out.println(RestauranteCRUDHibernate.listar(tabla, null, null));
                
                System.out.print("ID del registro a borrar: ");
                int id = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1 -> exito = RestauranteCRUDHibernate.borrarMesa(session, id);
                    case 2 -> exito = RestauranteCRUDHibernate.borrarProducto(session, id);
                    case 3 -> exito = RestauranteCRUDHibernate.borrarFactura(session, id);
                    case 4 -> exito = RestauranteCRUDHibernate.borrarPedido(session, id);
                }
            }

            if (!exito) {
                System.out.println("✗ No se pudo borrar.");
                transaction.rollback();
                return;
            }

            // Mostrar datos después del borrado
            System.out.println("\n--- DATOS DESPUÉS DEL BORRADO ---");
            System.out.println(RestauranteCRUDHibernate.listar(tabla, null, null));

            System.out.print("\n¿Confirmar cambios? (s/n): ");
            if (sc.nextLine().equalsIgnoreCase("s")) {
                transaction.commit();
                System.out.println("✓ Cambios guardados.");
            } else {
                transaction.rollback();
                System.out.println("✗ Cambios deshechos.");
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("✗ Error al borrar: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Gestiona la eliminación de tablas (DROP TABLE).
     * ADVERTENCIA: Esto elimina permanentemente las tablas.
     */
    public static void gestionEliminarTablas() {
        System.out.println("\n----- ELIMINAR TABLA (DROP) -----");
        System.out.println("⚠ ADVERTENCIA: Esta acción elimina la tabla permanentemente.");
        System.out.print("¿Eliminar todas las tablas? (s/n): ");
        boolean eliminarTodas = sc.nextLine().equalsIgnoreCase("s");

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean exito = false;

            if (eliminarTodas) {
                System.out.print("¿Estás seguro de eliminar TODAS las tablas? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    exito = RestauranteCRUDHibernate.eliminarTodasTablas(session);
                }
            } else {
                System.out.println("Tablas disponibles: Mesa, Productos, Factura, Pedido");
                System.out.println("Orden recomendado: Pedido → Factura → Productos → Mesa");
                System.out.print("Nombre de la tabla a eliminar: ");
                String tabla = sc.nextLine();
                
                System.out.print("¿Estás seguro de eliminar la tabla " + tabla + "? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    exito = RestauranteCRUDHibernate.eliminarTabla(session, tabla);
                }
            }

            if (!exito) {
                System.out.println("✗ No se pudo eliminar la(s) tabla(s).");
                transaction.rollback();
                return;
            }

            System.out.print("\n¿Confirmar eliminación? (s/n): ");
            if (sc.nextLine().equalsIgnoreCase("s")) {
                transaction.commit();
                System.out.println("✓ Tabla(s) eliminada(s) correctamente.");
            } else {
                transaction.rollback();
                System.out.println("✗ Eliminación cancelada.");
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("✗ Error al eliminar tabla(s): " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }
}
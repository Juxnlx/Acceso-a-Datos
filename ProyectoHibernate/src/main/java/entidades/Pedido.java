package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "Pedido")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPedido")
    private int idPedido;
    
    // Relación con Factura (muchos pedidos pueden pertenecer a una factura)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFactura", referencedColumnName = "idFactura")
    private Factura factura;
    
    // Relación con Producto (muchos pedidos pueden referenciar a un producto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    private Producto producto;
    
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    
    // Constructores
    public Pedido() {
    }
    
    public Pedido(Factura factura, Producto producto, int cantidad) {
        this.factura = factura;
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    
    public Factura getFactura() {
        return factura;
    }
    
    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", idFactura=" + (factura != null ? factura.getIdFactura() : "null") +
                ", idProducto=" + (producto != null ? producto.getIdProducto() : "null") +
                ", cantidad=" + cantidad +
                '}';
    }
}
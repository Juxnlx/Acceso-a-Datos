package entidades;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Factura")
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFactura")
    private int idFactura;
    
    // Relación con Mesa (muchas facturas pueden pertenecer a una mesa)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMesa", referencedColumnName = "idMesa")
    private Mesa mesa;
    
    @Column(name = "tipoPago", nullable = false, length = 45)
    private String tipoPago;
    
    @Column(name = "importe", nullable = false, precision = 10, scale = 2)
    private double importe;
    
    // Relación con Pedido (una factura puede tener muchos pedidos)
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;
    
    // Constructores
    public Factura() {
    }
    
    public Factura(Mesa mesa, String tipoPago, double importe) {
        this.mesa = mesa;
        this.tipoPago = tipoPago;
        this.importe = importe;
    }
    
    // Getters y Setters
    public int getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }
    
    public Mesa getMesa() {
        return mesa;
    }
    
    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    
    public String getTipoPago() {
        return tipoPago;
    }
    
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
    
    public double getImporte() {
        return importe;
    }
    
    public void setImporte(double importe) {
        this.importe = importe;
    }
    
    public List<Pedido> getPedidos() {
        return pedidos;
    }
    
    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    
    @Override
    public String toString() {
        return "Factura{" +
                "idFactura=" + idFactura +
                ", idMesa=" + (mesa != null ? mesa.getIdMesa() : "null") +
                ", tipoPago='" + tipoPago + '\'' +
                ", importe=" + importe +
                '}';
    }
}
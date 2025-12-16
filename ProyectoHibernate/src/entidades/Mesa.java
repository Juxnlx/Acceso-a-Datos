package entidades;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Mesa")
public class Mesa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMesa")
    private int idMesa;
    
    @Column(name = "numComensales", nullable = false)
    private int numComensales;
    
    @Column(name = "reserva", nullable = false)
    private int reserva;
    
    // Relación con Factura (una mesa puede tener muchas facturas)
    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Factura> facturas;
    
    // Constructores
    public Mesa() {
    }
    
    public Mesa(int numComensales, int reserva) {
        this.numComensales = numComensales;
        this.reserva = reserva;
    }
    
    // Getters y Setters
    public int getIdMesa() {
        return idMesa;
    }
    
    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }
    
    public int getNumComensales() {
        return numComensales;
    }
    
    public void setNumComensales(int numComensales) {
        this.numComensales = numComensales;
    }
    
    public int getReserva() {
        return reserva;
    }
    
    public void setReserva(int reserva) {
        this.reserva = reserva;
    }
    
    public List<Factura> getFacturas() {
        return facturas;
    }
    
    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }
    
    @Override
    public String toString() {
        return "Mesa{" +
                "idMesa=" + idMesa +
                ", numComensales=" + numComensales +
                ", reserva=" + reserva +
                '}';
    }
}
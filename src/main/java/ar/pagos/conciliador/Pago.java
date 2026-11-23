package ar.pagos.conciliador;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
public class Pago {
    @Id
    public String paymentId;
    public String estado;
    public BigDecimal monto;
    public String moneda;
    public LocalDateTime actualizado;
}

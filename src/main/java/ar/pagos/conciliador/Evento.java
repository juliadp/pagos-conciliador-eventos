package ar.pagos.conciliador;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
public class Evento {
    @Id
    public String eventId;
    public String paymentId;
    public String estado;
    public LocalDateTime fecha;
    public LocalDateTime recibido;
}

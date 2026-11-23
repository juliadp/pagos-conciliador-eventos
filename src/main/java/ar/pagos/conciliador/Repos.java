package ar.pagos.conciliador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface PagoRepo extends JpaRepository<Pago, String> {
}

interface EventoRepo extends JpaRepository<Evento, String> {
    List<Evento> findByPaymentIdOrderByFechaAsc(String paymentId);
}

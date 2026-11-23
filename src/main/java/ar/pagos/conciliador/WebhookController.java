package ar.pagos.conciliador;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
public class WebhookController {

    // estados finales segun el PO
    static final Set<String> FINALES = Set.of("CAPTURED", "FAILED", "REFUNDED");

    private final PagoRepo pagos;
    private final EventoRepo eventos;

    public WebhookController(PagoRepo pagos, EventoRepo eventos) {
        this.pagos = pagos;
        this.eventos = eventos;
    }

    public record WebhookBody(String eventId, String paymentId, String status,
                              BigDecimal amount, String currency, LocalDateTime timestamp) {}

    @PostMapping("/webhook")
    @Transactional
    public ResponseEntity<String> webhook(@RequestBody WebhookBody body) {
        if (eventos.existsById(body.eventId())) {
            return ResponseEntity.ok("ya procesado");
        }

        Evento ev = new Evento();
        ev.eventId = body.eventId();
        ev.paymentId = body.paymentId();
        ev.estado = body.status();
        ev.fecha = body.timestamp();
        ev.recibido = LocalDateTime.now();
        eventos.save(ev);

        Pago pago = pagos.findById(body.paymentId()).orElseGet(() -> {
            Pago p = new Pago();
            p.paymentId = body.paymentId();
            return p;
        });

        // si ya esta en un estado final no lo toco
        if (pago.estado == null || !FINALES.contains(pago.estado)) {
            pago.estado = body.status();
            pago.monto = body.amount();
            pago.moneda = body.currency();
        }
        pago.actualizado = LocalDateTime.now();
        pagos.save(pago);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/pagos/{id}")
    public ResponseEntity<Pago> pago(@PathVariable String id) {
        return pagos.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pagos/{id}/eventos")
    public List<Evento> eventos(@PathVariable String id) {
        return eventos.findByPaymentIdOrderByFechaAsc(id);
    }
}

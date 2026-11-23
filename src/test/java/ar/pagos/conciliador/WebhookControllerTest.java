package ar.pagos.conciliador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;MODE=MySQL",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
class WebhookControllerTest {

    @Autowired WebhookController controller;
    @Autowired EventoRepo eventos;
    @Autowired PagoRepo pagos;

    @Test
    void elMismoEventoDosVecesSeGuardaUnaSola() {
        var body = new WebhookController.WebhookBody("ev1", "p1", "PENDING",
                new BigDecimal("100"), "ARS", LocalDateTime.now());

        controller.webhook(body);
        var resp = controller.webhook(body);

        assertEquals("ya procesado", resp.getBody());
        assertEquals(1, eventos.findByPaymentIdOrderByFechaAsc("p1").size());
    }

    @Test
    void unPagoCapturadoNoCambiaMas() {
        controller.webhook(new WebhookController.WebhookBody("ev2", "p2", "CAPTURED",
                new BigDecimal("100"), "ARS", LocalDateTime.now()));
        controller.webhook(new WebhookController.WebhookBody("ev3", "p2", "PENDING",
                new BigDecimal("100"), "ARS", LocalDateTime.now()));

        assertEquals("CAPTURED", pagos.findById("p2").get().estado);
    }
}

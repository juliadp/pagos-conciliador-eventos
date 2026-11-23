# pagos-conciliador-eventos

servicio para recibir los webhooks de paygate y guardar el estado de los pagos

## endpoints

- `POST /webhook` recibe el evento de paygate (json en snake_case)
- `GET /pagos/{id}` estado del pago
- `GET /pagos/{id}/eventos` eventos del pago ordenados por fecha

## como funciona

- si el `event_id` ya existe devuelve 200 y no hace nada (paygate reintenta hasta 5 veces con el mismo id)
- si el pago ya esta en un estado final (CAPTURED, FAILED, REFUNDED) no se cambia mas

## correrlo

necesita un mysql local con una base `pagos` (usuario root/root)

```
mvn spring-boot:run
```

tests:

```
mvn test
```

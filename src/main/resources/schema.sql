create table if not exists pago (
  payment_id varchar(50) primary key,
  estado varchar(20),
  monto decimal(12,2),
  moneda varchar(3),
  actualizado timestamp
);

create table if not exists evento (
  event_id varchar(50) primary key,
  payment_id varchar(50),
  estado varchar(20),
  fecha timestamp,
  recibido timestamp
);

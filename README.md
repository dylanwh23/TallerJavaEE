# TallerJava  
**Proyecto:** Taller Java 2025  
**Autores:** Dylan Rodriguez, Diego Pozzi, Matías Rodríguez, Federico Olivera  
**Implementación:** Pasarela de Pagos  

---

## Diseño

### Diagrama de Clases

- **Módulo Comercio**  
  ![Comercio Module](https://github.com/user-attachments/assets/5a3a3874-a071-4a79-a859-4455eff89e06)

- **Módulo Compras**  
  ![Compras Module](https://github.com/user-attachments/assets/a79c6b3b-1c45-4af1-9dcf-00f11dbba15c)

- **Módulo Transferencias**  
  ![Transferencias Module](https://github.com/user-attachments/assets/f8363053-761c-4963-896e-a8e8c456cb4d)

- **Módulo Monitoreo**  
  *No cuenta con clases*

---

### Diagrama de Casos de Uso / Secuencias

- **Alta comercios**  
  ![Alta comercios](https://github.com/user-attachments/assets/0c396dd2-4d85-4524-8cb8-4824ab5a5455)

- **Alta POS**  
  ![Alta POS](https://github.com/user-attachments/assets/4e3af4ab-c5b7-4144-bac3-6dece8592bbb)

- **Cambiar estado POS**  
  ![Cambiar estado POS](https://github.com/user-attachments/assets/fe49c917-204c-4d75-8d6c-4bd4a0bd86be)

- **Cambio contraseña**  
  ![Cambio contraseña](https://github.com/user-attachments/assets/69ee1c03-8f62-4f33-a5a4-f846fbc6e979)

- **Realizar reclamo**  
  ![Realizar reclamo](https://github.com/user-attachments/assets/89369427-d379-4f0b-bd81-ba17adfa21df)

- **Resumen ventas diarias**  
  ![Ventas diarias](https://github.com/user-attachments/assets/b33f1c1f-c65d-479e-b951-1f0f188e5e02)

- **Resumen ventas por periodo**  
  ![Ventas periodo](https://github.com/user-attachments/assets/e0d8c8bd-19be-4493-90d6-8f886ddcd993)

- **Monto actual vendido**  
  ![Monto actual vendido](https://github.com/user-attachments/assets/03559a2c-cfa3-43c1-9cc4-4bbc6f9bba41)

- **Notificación Transferencia Desde Medio de Pago**  
  ![Notificación Transferencia](https://github.com/user-attachments/assets/eda1561b-69f8-4b02-a90d-f0351ce244ad)

- **Consultar Depósitos (comercios, rangoFechas)**  
  ![Consultar depósitos](https://github.com/user-attachments/assets/e977a37f-f8f2-4584-a17d-28538dc1c81f)

---

### Diagramas de Paquetes

![Diagramas de Paquetes](https://github.com/user-attachments/assets/c87e9676-b949-42dd-a860-beb536cef572)

---

### Diagrama de módulo Pasarela de Pagos

![Módulo Pasarela de Pagos](https://github.com/user-attachments/assets/8bdbd892-1a53-4bf0-981f-c15cb9b36dc2)

---

## Arquitectura

![Arquitectura General](https://github.com/user-attachments/assets/7f2cff00-9f69-490c-a6d7-c964830db0b3)

- **Comercio**  
  - **POS:** llamadas al API  
  - **Browser:** cliente web para registro, estadísticas, reclamos, etc.

- **Server Pasarela**  
  - **Gestor Web:** página web del cliente  
  - **Pasarela de Pagos:** núcleo que expone las funcionalidades  
  - **Medio de pago:** mock del gateway real  
  - **Banco cliente:** mock del sistema bancario  
  - **ArtemisMQ:** colas JMS en WildFly para encolar reclamos  
  - **ReclamosAIClient:** modelo de IA que procesa reclamos en paralelo  
  - **DATABASE:** base de datos relacional

- **Docker Container (Observabilidad)**  
  - **Grafana:** dashboards de monitoreo (InfluxDB)  
  - **InfluxDB:** almacena métricas temporales  
  - **Telegraf:** recoge métricas de la pasarela y las envía a InfluxDB

---

## Funcionalidades

### Rate Limiter

El Rate Limiter (Leaky Bucket) funciona así:

1. Balde vacío con límite de tokens.  
2. Cada petición añade un token al balde.  
3. Las peticiones se procesan a ritmo constante (una gota a la vez).  
4. Si el balde se llena, nuevas peticiones se rechazan.  
5. Garantiza procesamiento secuencial y evita sobrecarga.  

**Prueba en JMeter (rechazo de peticiones excesivas):**  
![Rate Limiter JMeter](https://github.com/user-attachments/assets/ef7d5b5a-eefb-4c75-9a7a-2f8fd665018e)

---

### Observabilidad

- **Micrometer/JMX → Telegraf → InfluxDB → Grafana**  
- Métricas de latencia y throughput:

  - **Endpoints Comercio (latencia ms):**  
    ![Latencia Comercio](https://github.com/user-attachments/assets/96c27f12-a512-4875-88cb-c6260b8764a6)

  - **Throughput Compras:**  
    ![Throughput Compras](https://github.com/user-attachments/assets/9d65bcb3-f1b4-45d1-9cdd-aca10fb6fc5e)

  - **Recursos Docker (CPU/Memoria):**  
    ![Recursos Docker](https://github.com/user-attachments/assets/4dbaa30e-ac2d-4330-b1ca-5daaf070a3e9)

---

## Endpoints

### Comercio

| Operación             | Método | Consumes           | Parámetros / Body                                        | Respuesta                                              |
|-----------------------|:------:|--------------------|----------------------------------------------------------|--------------------------------------------------------|
| Registrar comercio    | POST   | `application/json` | `{ "nombre","correo","telefono","contrasenia" }`         | `200 OK` — "Registro satisfactorio."                    |
| Agregar POS           | POST   | Form params        | `correo=p&contr=...`                                     | `200 OK` — "POS agregado satisfactoriamente."           |
| Realizar reclamo      | POST   | `application/json` | `AuthRequest + { "texto": "..." }`                       | `200 OK` — "Reclamo realizado satisfactoriamente."      |
| Modificar comercio    | POST   | `application/json` | `AuthRequest + { nombre,telefono,nuevoCorreo,nuevaContrasenia }` | `200 OK` — "Datos modificados satisfactoriamente."      |
| Cambiar estado de POS | POST   | `application/json` | `AuthRequest + { idPOS, estado }`                        | `200 OK` — "Estado del POS modificado satisfactoriamente." |

### Compra

| Operación            | Método | Consumes           | Parámetros / Body                                                                                          | Respuesta                                |
|----------------------|:------:|--------------------|-------------------------------------------------------------------------------------------------------------|------------------------------------------|
| Pago simple          | POST   | Query-string       | `?idComercio=&idPos=&monto=&numero=&cvv=&propietario=&vencimiento=`                                         | `200 OK` — JSON `Compra`                 |
| Pago (JSON)          | POST   | `application/json` | `{ "idComercio","idPos","monto","dataTarjeta":{numero,cvv,vencimiento,propietario} }`                       | `201 Created` — JSON `Compra`            |
| Ventas diarias       | GET    | `application/json` | `?idComercio=`                                                                                              | `200 OK` — `[Compra,…]`                  |
| Ventas por periodo   | GET    | `application/json` | `?idComercio=&desde=YYYY-MM-DD&hasta=YYYY-MM-DD`                                                           | `200 OK` — `[Compra,…]`                  |
| Monto actual vendido | GET    | `application/json` | `?idComercio=`                                                                                              | `200 OK` — `{ "montoActualVendido": float }` |

### Transferencia

| Operación            | Método | Produces           | Parámetros                                      | Respuesta                     |
|----------------------|:------:|--------------------|-------------------------------------------------|-------------------------------|
| Ventas por periodo   | GET    | `application/json` | `?idComercio=&desde=YYYY-MM-DD&hasta=YYYY-MM-DD`| `200 OK` — `[Transferencia,…]` |

---

## Ejemplos de `curl`

```bash
# Ventas diarias
curl -X GET "http://localhost:8080/TallerJavaEE-1.0-SNAPSHOT/api/compra/ventasDiarias?idComercio=1"

# Ventas por periodo
curl "http://localhost:8080/TallerJavaEE-1.0-SNAPSHOT/api/compra/ventasPeriodo?idComercio=1&desde=2025-05-01&hasta=2025-05-23"

# Monto actual vendido
curl -X GET "http://localhost:8080/TallerJavaEE-1.0-SNAPSHOT/api/compra/montoActualVendido?idComercio=1"

# Procesar pago simple
curl --% -X POST "http://localhost:8080/TallerJavaEE-1.0-SNAPSHOT/api/compra/pago-simple?idComercio=1&idPos=1&monto=5400&numero=1232&cvv=143&propietario=Machi&vencimiento=2025-05-30T18:43:45.000Z"

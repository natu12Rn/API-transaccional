

# API Transaccional

Este proyecto es un sistema transaccional que permite a los usuarios realizar diversas operaciones, tales como iniciar sesión, consultar información de la cuenta, realizar retiros, depósitos, transferencias y acceder a un registro detallado de todos los movimientos realizados.



## Características

- **Registro de nuevos usuarios.**
- **Inicio de sesión:** Autenticación mediante credenciales.
- **Operaciones transaccionales:** Incluye retiros, depósitos y transferencias.
- **Consulta de cuenta:** Acceso a los detalles de la cuenta del usuario.
- **Historial de transacciones:** Registro completo de las transacciones realizadas.


## Rutas

### Login

- **Método:** `POST`
- **URL:** `https://localhost:8080/api/login`
- **Descripción:** Permite iniciar sesión mediante credenciales (nombre de usuario y contraseña). Devuelve un token JWT para la autenticación.

#### Body
```json
{
  "login": "pablo9",
  "password": "pablo9"
}
```

#### Parámetros

| Variable | Descripción             |
|----------|-------------------------|
| login    | Nombre de usuario.      |
| password | Contraseña del usuario. |

#### Ejemplo de respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### Registro de usuario

- **Método:** `POST`
- **URL:** `https://localhost:8080/api/registrar`
- **Descripción:** Permite registrar nuevos usuarios y crear sus cuentas bancarias proporcionando datos como correo electrónico, nombre, contraseña y tipo de cuenta.

#### Body
```json
{
  "login": "carlosDiaz45",
  "password": "contraseñaFuerte123",
  "passwordConfirm": "contraseñaFuerte123",
  "email": "carlos.diaz@example.com",
  "name": "Carlos Díaz",
  "tipo": "Inversión"
}
```

#### Parámetros

| Variable        | Descripción                                         |
|-----------------|-----------------------------------------------------|
| login           | Nombre de usuario único.                            |
| password        | Contraseña del usuario.                             |
| passwordConfirm | Confirmación de la contraseña.                      |
| email           | Correo electrónico del usuario.                     |
| name            | Nombre completo del usuario.                        |
| tipo            | Tipo de cuenta: "Ahorro", "Corriente" o "Inversión" |

#### Ejemplo de respuesta:
```json
{
  "password": "b0e9e8cfb872a8ef053f1cc74106182c",
  "name": "Carlos Díaz",
  "cuenta": {
    "numCuenta": "249786167597",
    "tipoCuenta": "Inversión",
    "saldo": 0
  },
  "login": "carlosDiaz45",
  "email": "carlos.diaz@example.com"
}
```

---

### Depósitos

- **Método:** `POST`
- **URL:** `https://localhost:8080/api/transaccion`
- **Autenticación:** Bearer Token
- **Descripción:** Permite depositar dinero en la cuenta del usuario.

#### Body
```json
{
  "tipo_transaccion": "deposito",
  "monto": 10000,
  "descripcion": "",
  "cuenta_destino": ""
}
```

#### Parámetros

| Variable         | Descripción                                           |
|------------------|-------------------------------------------------------|
| tipo_transaccion | Tipo de transacción ("deposito").                     |
| monto            | Monto del depósito.                                   |
| descripcion      | Descripción opcional (no aplica para depósitos).      |
| cuenta_destino   | No aplica para depósitos.                             |

#### Ejemplo de respuesta:
```json
{
  "descripcion": "",
  "monto": 10000,
  "cuentaDestino": "",
  "cuentaId": 2,
  "tipoTransaccion": "deposito",
  "transaccionId": 0
}
```

---

### Retiros

- **Método:** `POST`
- **URL:** `https://localhost:8080/api/transaccion`
- **Autenticación:** Bearer Token
- **Descripción:** Permite retirar una cantidad específica de dinero. El monto mínimo permitido es de $10,000.00.

#### Body
```json
{
  "tipo_transaccion": "retiro",
  "monto": 10000,
  "descripcion": "",
  "cuenta_destino": ""
}
```

#### Parámetros

| Variable         | Descripción                           |
|------------------|---------------------------------------|
| tipo_transaccion | Tipo de transacción ("retiro").       |
| monto            | Monto del retiro.                     |
| descripcion      | Descripción opcional del retiro.      |
| cuenta_destino   | No aplica para retiros.               |

#### Ejemplo de respuesta:
```json
{
  "descripcion": "",
  "monto": 10000,
  "cuentaDestino": "",
  "cuentaId": 2,
  "tipoTransaccion": "retiro",
  "transaccionId": 0
}
```

---

### Transferencias

- **Método:** `POST`
- **URL:** `https://localhost:8080/api/transaccion`
- **Autenticación:** Bearer Token
- **Descripción:** Permite transferir dinero a otra cuenta mediante el número de cuenta destino.

#### Body
```json
{
  "tipo_transaccion": "transferencia",
  "monto": 500,
  "descripcion": "",
  "cuenta_destino": "506216641172"
}
```

#### Parámetros

| Variable         | Descripción                                    |
|------------------|------------------------------------------------|
| tipo_transaccion | Tipo de transacción ("transferencia").         |
| monto            | Monto de la transferencia.                     |
| descripcion      | Descripción opcional de la transferencia.      |
| cuenta_destino   | Número de cuenta de destino.                   |

#### Ejemplo de respuesta:
```json
{
  "descripcion": "",
  "monto": 500,
  "cuentaDestino": "506216641172",
  "cuentaId": 2,
  "tipoTransaccion": "transferencia",
  "transaccionId": 0
}
```

---

### Consulta de cuenta

- **Método:** `GET`
- **URL:** `https://localhost:8080/api/cuenta`
- **Autenticación:** Bearer Token
- **Descripción:** Permite obtener información de la cuenta, como número de cuenta, saldo y tipo de cuenta.

**Detalles adicionales:** Si el usuario se autentifica como Administrador, la ruta devolverá un listado con todas las cuentas disponibles, incluyendo número de cuenta, tipo de cuenta y saldo.

#### Ejemplo de respuesta (Usuario):
```json
{
  "numCuenta": "176415451443",
  "tipoCuenta": "Ahorros",
  "saldo": 11501
}
```
#### Ejemplo de respuesta (Admin):
```json
[
    {
        "tipo_cuenta": "Ahorros",
        "name": "erick",
        "numero_cuenta": "249381125604",
        "login": "erick",
        "email": "erick@gmail.com"
    },
    {
        "tipo_cuenta": "Ahorros",
        "name": "",
        "numero_cuenta": "927464150960",
        "login": "alex1",
        "email": "alex@gmai.com"
    },
    {
        "tipo_cuenta": "Ahorros",
        "name": "",
        "numero_cuenta": "421920917299",
        "login": "robert",
        "email": "robert@gmail.com"
    }
]
```

---

### Historial de transacciones

- **Método:** `GET`
- **URL:** `https://localhost:8080/api/transaccion`
- **Autenticación:** Bearer Token
- **Descripción:** Permite visualizar todas las transacciones realizadas en la cuenta.

**Detalles adicionales:** Si el usuario se autentica como Administrador, la ruta devolverá el historial completo de todas las transacciones de todas las cuentas, incluyendo el número de cuenta asociado.

#### Ejemplo de respuesta (Usuario):
```json
[
  {
    "tipo_transaccion": "transferencia",
    "descripcion": "",
    "monto": 500,
    "fecha_transaccion": "2025-01-09 15:33:13.159574",
    "numero_cuenta": "176415451443",
    "transaccion_id": 103,
    "cuenta_destino": "506216641172"
  },
  {
    "tipo_transaccion": "depósito",
    "monto": 10000,
    "fecha_transaccion": "2025-01-09 15:40:56.999825",
    "numero_cuenta": "176415451443",
    "transaccion_id": 104
  },
  {
    "tipo_transaccion": "retiro",
    "descripcion": "",
    "monto": 10000,
    "fecha_transaccion": "2025-01-09 15:41:06.325294",
    "numero_cuenta": "176415451443",
    "transaccion_id": 105
  }
]
```
#### Ejemplo de respuesta (Admin):
```json
[
  {
    "tipo_transaccion": "transferencia",
    "descripcion": "",
    "monto": 500,
    "fecha_transaccion": "2025-01-09 12:20:46.527135",
    "numero_cuenta": "985807879578",
    "transaccion_id": 99,
    "cuenta_destino": "506216641172"
  },
  {
    "tipo_transaccion": "depósito",
    "monto": 10000,
    "fecha_transaccion": "2025-01-09 12:19:11.297734",
    "numero_cuenta": "985807879578",
    "transaccion_id": 98
  },
  {
    "tipo_transaccion": "retiro",
    "descripcion": "",
    "monto": 10000,
    "fecha_transaccion": "2025-01-09 10:58:01.060276",
    "numero_cuenta": "985807879578",
    "transaccion_id": 97
  }
]
```


## Tecnologías utilizadas
- **java**
- **Gradle**
- **PostgreSQL**


## Dependecias utilizadas
- **JSON In java (org.json)**
- **JWT**
- **PostgreSQL JDBC Driver**

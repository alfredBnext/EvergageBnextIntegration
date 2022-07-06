# Introduction 
Plugin que conecta evergage con tu proyecto de cordova.

# Instalation
```bash
$ ionic cordova plugin add https://github.com/alfredBnext/EvergageBnextIntegration.git --variable EVERGAGE_SCHEME=""
```

# Métodos
- setUserId
- start
- setLogLevel
- viewProduct
- viewCategory
- addToCart
- trackAction
- purchase

------------


## Set User Id
### Descripción
Evento para regristrar un identificador al usuario

### Método
setUserId(userId, success, error)


### Paremetros del método
**User Id**: string

------------


## Start
### Descripción
Evento con el que indicamos el comienzo evergage pasandole los parametros para tener en cuenta los usos y url a donde apuntara.

### Método
start(account, dataset, usePushNotification, success, error)


### Paremetros del método
**Acount**: string
**Dataset**: string
**Use Push Notification**: bool

------------


## Set Log Level
### Descripción
Evento para poder definir el nivel de log de evergage nos permite que el las consolas de las plataformas (Logcat, terminal) en android y ios respectivamente. Nos de informacion dependiendo del numero que se le indique. A continuacion se muestran las variables que estan definidas en el propio plugin:

- OFF: 0
- ERROR: 1000
- WARN: 2000
- INFO: 3000
- DEBUG: 4000
- ALL: 2147483647

### Método
setLogLevel(errorLevel, success, error)


### Paremetros del método
**Error Level**: numeric

------------


## View Product
### Descripción
Evento cuya finalidad es guardar cuando abren la vista de un producto

### Método
viewProduct(id, name, price, success, error)


### Paremetros del método
**Id**: String
**Name**: String
**Price**:  decimal

------------


## View Category
### Descripción
Evento cuya finalidad es guardar cuando abren la vista de una categoria

### Método
viewCategory(id, name, success, error)


### Paremetros del método
**Id**: String
**Name**: String

------------


## Add To Cart
### Descripción
Evento para registar al agregar productos al carrito

### Método
addToCart(id, name, price, quantity, success, error)


### Paremetros del método
**Id**: String
**Name**: String
**Price**:  decimal
**Quantity**:  Int

------------


## Track Action
### Descripción
Evento para registar eventos no contemplados mediante un unico texto que recibe commo parametro

### Método
trackAction(event, success, error)


### Paremetros del método
**Event**: String

------------
## Purchase
### Descripción
Evento para regristrar una venta, este evento tiene la peculiaridad que las lineas se envian como string con el json del objeto que se muestra a continuacion. Debajo mostraremos el método con los parametros que ocupa.

### Método
purchase(orderId, lines, totalevent, success, error)


### Paremetros del método
**Order id**: string
**Lines**: Json string en el siguiente formato
```json
{
    "list": [
        {
            "id": "1",
            "name": "Galletas",
            "price" : 15.1,
            "quantity": 1
        }
    ]
}
```
**TotalEvent**: numeric

------------
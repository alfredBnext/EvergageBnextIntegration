# Introduction 
Plugin que conecta evergage con tu proyecto de cordova

# Instalation
```bash
$ ionic cordova plugin add https://github.com/AlfredRds/EvergageBnextIntegration.git --variable EVERGAGE_SCHEME=""
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

## Purchase
Evento para regristrar una venta, este evento tiene la peculiaridad que las lineas se envian como string con el json del objeto que se muestra a continuacion. Debajo mostraremos el método con los parametros que ocupa.

**purchase(orderId, lines, totalevent, success, error)
purchase(string, json en string, decimal, lamda, lamda)**

------------


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
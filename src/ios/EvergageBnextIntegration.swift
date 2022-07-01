import Evergage

@objc(EvergageBnextIntegration) class EvergageBnextIntegration: CDVPlugin {
    @objc(setUserId:)
    func setUserId(command: CDVInvokedUrlCommand) {
        let arguments = command.argument(at: 0) as? Dictionary<String, Any>
        let userId = arguments?["userId"] as? String
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        
        evergage.userId = userId
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
    @objc(start:)
    func start(command: CDVInvokedUrlCommand) {
        let arguments = command.argument(at: 0) as? Dictionary<String, Any>
        let account = arguments?["account"] as? String
        let dataset = arguments?["dataset"] as? String
        let usePushNotification = arguments?["usePushNotification"] as? Bool
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        
        if(account != nil && dataset != nil && usePushNotification != nil) {
            evergage.start { (clientConfigurationBuilder) in
                clientConfigurationBuilder.account = account!
                clientConfigurationBuilder.dataset = dataset!
                clientConfigurationBuilder.usePushNotifications = usePushNotification!
            }
        }
        
        
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
    @objc(setLogLevel:)
    func setLogLevel(command: CDVInvokedUrlCommand) {
        let arguments = command.argument(at: 0) as? Dictionary<String, Any>
        let errorLevel = arguments?["errorLevel"] as? String
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        
        if(errorLevel != nil){
            let logLevel = Int(errorLevel!)
            
            if let loglevenEvg = EVGLogLevel (rawValue: logLevel!) {
                evergage.logLevel = loglevenEvg
            }
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(viewProduct:)
    func viewProduct(command: CDVInvokedUrlCommand) {
        let arguments = command.argument(at: 0) as? Dictionary<String, Any>
        let id = arguments?["id"] as? String
        let name = arguments?["name"] as? String
        let price = arguments?["price"] as? Double
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        
        if(id != nil && name != nil && price != nil){
            let product = EVGProduct.init(id: String(id!))
            product.name = name!
            product.price = NSNumber(value: price!)
            evergage.globalContext?.viewItem(product)
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(viewCategory:)
    func viewCategory(command: CDVInvokedUrlCommand) {
        let arguments = command.argument(at: 0) as? Dictionary<String, Any>
        let id = arguments?["id"] as? String
        let name = arguments?["name"] as? String
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        
        if (id != nil) {
            let category = EVGCategory.init(id: id!)
            category.name = name
            evergage.globalContext?.viewCategory(category)
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(trackAction:)
    func trackAction(command: CDVInvokedUrlCommand) {
        let arguments = command.argument(at: 0) as? Dictionary<String, Any>
        let event = arguments?["event"] as? String
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        
        if (event != nil){
            evergage.globalContext?.trackAction(event!)
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
    @objc(addToCart:)
    func addToCart(command: CDVInvokedUrlCommand) {
        let arguments = command.argument(at: 0) as? Dictionary<String, Any>
        let id = arguments?["id"] as? String
        let name = arguments?["name"] as? String
        let price = arguments?["price"] as? Double
        let quantity = arguments?["quantity"] as? Int
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        
        if (id != nil && name != nil && price != nil && quantity != nil) {
            let product = EVGProduct.init(id: id!)
            product.name = name
            product.price = NSNumber(value: price!)
            evergage.globalContext?.add(toCart: (EVGLineItem.init(item: product, quantity: NSNumber(value: quantity!))))
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
}

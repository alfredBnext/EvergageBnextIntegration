import Evergage
import Foundation
import CommonCrypto


@objc(EvergageBnextIntegration) class EvergageBnextIntegration: CDVPlugin {
    @objc(setUserId:)
    func setUserId(command: CDVInvokedUrlCommand) {
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let userId = command.argument(at: 0) as? String
        let email = command.argument(at: 1) as? String
        let firstName = command.argument(at: 2) as? String
        let lastName = command.argument(at: 3) as? String
        evergage.userId = userId
        evergage.setUserAttribute(email, forName: "emailAddress")
        evergage.setUserAttribute(email?.sha256(), forName: "emailSHA256")
        evergage.setUserAttribute(firstName, forName: "firstName")
        evergage.setUserAttribute(lastName, forName: "lastName")
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
    @objc(start:)
    func start(command: CDVInvokedUrlCommand) {
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let account = command.argument(at: 0) as? String
        let dataset = command.argument(at: 1) as? String
        let usePushNotification = command.argument(at: 2) as? Bool
        
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
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let errorLevel = command.argument(at: 0) as? Int
        
        if(errorLevel != nil){
            if let loglevenEvg = EVGLogLevel (rawValue: errorLevel!) {
                evergage.logLevel = loglevenEvg
            }
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(viewProduct:)
    func viewProduct(command: CDVInvokedUrlCommand) {
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let id = command.argument(at: 0) as? String
        let name = command.argument(at: 1) as? String
        
        if(id != nil && name != nil){
            let product = EVGProduct.init(id: String(id!))
            product.name = name!
            evergage.globalContext?.viewItem(product)
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(viewCategory:)
    func viewCategory(command: CDVInvokedUrlCommand) {
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let id = command.argument(at: 0) as? String
        let name = command.argument(at: 1) as? String
        
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
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let event = command.argument(at: 0) as? String
        
        if (event != nil){
            evergage.globalContext?.trackAction(event!)
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
    @objc(addToCart:)
    func addToCart(command: CDVInvokedUrlCommand) {
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let id = command.argument(at: 0) as? String
        let name = command.argument(at: 1) as? String
        let price = command.argument(at: 2) as? Double
        let quantity = command.argument(at: 3) as? Int
        
        if (id != nil && name != nil && price != nil && quantity != nil) {
            let product = EVGProduct.init(id: id!)
            product.name = name
            product.price = NSNumber(value: price!)
            evergage.globalContext?.add(toCart: (EVGLineItem.init(item: product, quantity: NSNumber(value: quantity!))))
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
    @objc(purchase:)
    func purchase(command: CDVInvokedUrlCommand){
        var pluginResult: CDVPluginResult
        let evergage = Evergage.sharedInstance()
        let orderId = command.argument(at: 0) as? String
        let lines = command.argument(at: 1) as? String
        let total = command.argument(at: 2) as? Double
        
        if(orderId != nil && lines != nil && total != nil){
            let linesDecodes = try! JSONDecoder().decode(ListSaleLine.self, from: lines!.data(using: String.Encoding.utf8)!)
            
            let saleLines: [SaleLine] = linesDecodes.list;
            var linesEvent: [EVGLineItem] = []
            
            for saleLine in saleLines {
                let product = EVGProduct.init(id: saleLine.id)
                product.name = saleLine.name
                product.price = NSNumber(value: saleLine.price)
                linesEvent.append(EVGLineItem.init(item: product, quantity: NSNumber(value: saleLine.quantity)))
            }

            let order = EVGOrder.init(id: orderId, lineItems: linesEvent, totalValue: NSNumber(value: total!))
            evergage.globalContext?.purchase(order)
        }
        
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
}
public extension Data{
    func sha256() -> String{
        return hexStringFromData(input: digest(input: self as NSData))
    }
    
    private func digest(input : NSData) -> NSData {
        let digestLength = Int(CC_SHA256_DIGEST_LENGTH)
        var hash = [UInt8](repeating: 0, count: digestLength)
        CC_SHA256(input.bytes, UInt32(input.length), &hash)
        return NSData(bytes: hash, length: digestLength)
    }
    
    private  func hexStringFromData(input: NSData) -> String {
        var bytes = [UInt8](repeating: 0, count: input.length)
        input.getBytes(&bytes, length: input.length)
        
        var hexString = ""
        for byte in bytes {
            hexString += String(format:"%02x", UInt8(byte))
        }
        
        return hexString
    }
}

public extension String {
    func sha256() -> String{
        if let stringData = self.data(using: String.Encoding.utf8) {
            return stringData.sha256()
        }
        return ""
    }
}

@objc(EvergageBnextIntegration) class EvergageBnextIntegration: CDVPlugin {
    @objc(setUserId:)
    func setUserId(command: CDVInvokedUrlCommand) {
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
    @objc(getSessionId:)
    func getSessionId(command: CDVInvokedUrlCommand) {
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(start:)
    func start(command: CDVInvokedUrlCommand) {
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(setLogLevel:)
    func setLogLevel(command: CDVInvokedUrlCommand) {
        let sessionId = DLDataCollectorSDK.DLCollector.shared.getSessionId()
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: sessionId ?? "")
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(viewProduct:)
    func viewProduct(command: CDVInvokedUrlCommand) {
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(viewCategory:)
    func viewCategory(command: CDVInvokedUrlCommand) {
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }

    @objc(trackAction:)
    func trackAction(command: CDVInvokedUrlCommand) {
        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
}
var exec = require('cordova/exec');

module.exports = {
    
    coolMethod: function (arg0, success, error) {
        exec(success, error, 'EvergageBnextIntegration', 'coolMethod', [arg0]);
    },

    setUserId(userId, success, error){
        exec(success, error, 'EvergageBnextIntegration', 'setUserId', [userId])
    },

    start: function(account, dataset, usePushNotification, success, error){
        exec(success, error,'EvergageBnextIntegration', 'start', [account, dataset, usePushNotification]);
    },

    viewProduct(id, name, price, success, error){
        exec(success, error, 'EvergageBnextIntegration', 'viewProduct', [id, name, price])
    },

    viewCategory(id, name, success, error){
        exec(success, error, 'EvergageBnextIntegration', 'viewCategory', [id, name])
    },

    trackAction(event, success, error){
        exec(success, error, 'EvergageBnextIntegration', 'viewCategory', [event])
    }
};

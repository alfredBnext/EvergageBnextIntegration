var exec = require('cordova/exec');
const LogError = {
     OFF: 0,

    /** Indicates that an operation failed. */
     ERROR: 1000,

    /** Indicates that something abnormal happened but the overall operation did not necessarily fail. */
     WARN: 2000,

    /** Provides detail about most operations. */
     INFO: 3000,

    /** Provides a high level of detail primarily intended for Evergage developers. */
     DEBUG: 4000,

    /** Enables all logging. */
     ALL: 2147483647
}

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

    setLogLevel: function(errorLevel, success, error){
        if(Object.values(LogError).includes(errorLevel)){
            exec(success, error, 'EvergageBnextIntegration', 'viewProduct', [id, name, price])
        }else{
            error('error level not found');
        }
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

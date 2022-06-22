package mx.bnext.EvergageBnextIntegration;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.evergage.android.ClientConfiguration;
import com.evergage.android.Evergage;
import com.evergage.android.LogLevel;
import com.evergage.android.Screen;

public class EvergageBnextIntegration extends CordovaPlugin {
    Evergage evergage;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Evergage.initialize(this.cordova.getActivity().getApplicationContext());
        Evergage evergage = Evergage.getInstance();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }

        if (action.equals("start")) {
            String account = args.getString(0);
            String dataset = args.getString(1);
            boolean usePushNotification = args.getBoolean(2);
            this.start(account, dataset, usePushNotification);
            return true;
        }

        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void start(String account, String dateset, boolean usePushNotification, CallbackContext callbackContext){
        try{
            evergage.start(new ClientConfiguration.Builder()
                .account(account)
                .dataset(dateset)
                .usePushNotifications(usePushNotification)
                .build());
            callbackContext.success(message);
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }
}

package mx.bnext.EvergageBnextIntegration;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
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
        Evergage.initialize(this.cordova.getActivity().getApplication());
        evergage = Evergage.getInstance();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("start")) {
            String account = args.getString(0);
            String dataset = args.getString(1);
            boolean usePushNotification = args.getBoolean(2);
            this.start(account, dataset, usePushNotification, callbackContext);
            return true;
        }

        return false;
    }

    private void start(String account, String dateset, boolean usePushNotification, CallbackContext callbackContext){
        try{
            evergage.start(new ClientConfiguration.Builder()
                .account(account)
                .dataset(dateset)
                .usePushNotifications(usePushNotification)
                .build());
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }
}

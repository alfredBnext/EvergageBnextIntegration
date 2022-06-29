package mx.bnext.EvergageBnextIntegration;

import android.util.Log;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import com.evergage.android.ClientConfiguration;
import com.evergage.android.Evergage;
import com.evergage.android.LogLevel;
import com.evergage.android.Screen;
import com.evergage.android.promote.Category;
import com.evergage.android.promote.LineItem;
import com.evergage.android.promote.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EvergageBnextIntegration extends CordovaPlugin {
    Evergage evergage;

    public List<String> actionsContemplated = new ArrayList<>(Arrays.asList(
            "start",
            "setLogLevel",
            "viewProduct",
            "viewCategory",
            "addToCart",
            "trackAction"
    ));
    public List<Integer> logLevelAccepted = new ArrayList<>(Arrays.asList(
            LogLevel.OFF,
            LogLevel.ERROR,
            LogLevel.WARN,
            LogLevel.INFO,
            LogLevel.DEBUG,
            LogLevel.ALL
    ));

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean haveAction = actionsContemplated.contains(action);

        if(haveAction){
            cordova.getActivity().runOnUiThread(() -> {
                try {
                    executeActions(action, args, callbackContext);
                } catch (JSONException e) {
                    callbackContext.error(e.toString());
                }
            });
        }

        return haveAction;
    }

    public void executeActions(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        initExecute();
        String id;
        String name;
        double price;
        switch (action){
            case "start":
                String account = args.getString(0);
                String dataset = args.getString(1);
                boolean usePushNotification = args.getBoolean(2);
                this.start(account, dataset, usePushNotification, callbackContext);
                break;
            case "setLogLevel":
                String eventLogString = args.getString(0);
                try {
                    int x = Integer.parseInt(eventLogString);
                    if(logLevelAccepted.contains(x))
                        this.setLogLevel(x, callbackContext);
                } catch (NumberFormatException ignored){
                    callbackContext.error("Level not exist");
                }
                break;
            case "viewProduct":
                id = args.getString(0);
                name = args.getString(1);
                price = args.getDouble(2);
                this.viewProduct(id, name, price, callbackContext);
                break;
            case "viewCategory":
                id = args.getString(0);
                name = args.getString(1);
                this.viewCategory(id, name, callbackContext);
                break;
            case "addToCart":
                id = args.getString(0);
                name = args.getString(1);
                price = args.getDouble(2);
                int quantity = args.getInt(3);
                this.addToCart(id, name, price, quantity, callbackContext);
                break;
            case "trackAction":
                String event = args.getString(0);
                this.trackAction(event, callbackContext);
                break;
        }
    }

    private void initExecute(){
        evergage = Evergage.getInstance();
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

    private void setLogLevel(@LogLevel final int logLevel, CallbackContext callbackContext){
        try {
            Evergage.setLogLevel(logLevel);
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }

    private void viewProduct(String id, String name, Double price, CallbackContext callbackContext){
        try {
            Screen screen = evergage.getScreenForActivity(this.cordova.getActivity());
            Product product = new Product(id);
            product.name = name;
            product.price = price;
            if(screen != null)
                screen.viewItem(product);
            else
                Objects.requireNonNull(evergage.getGlobalContext()).viewItem(product);
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }

    private void viewCategory(String id, String name, CallbackContext callbackContext){
        try {
            Screen screen = evergage.getScreenForActivity(this.cordova.getActivity());
            Category category = new Category(id);
            category.name = name;
            if(screen != null)
                screen.viewCategory(category);
            else
                Objects.requireNonNull(evergage.getGlobalContext()).viewCategory(category);
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }

    private void addToCart(String id, String name, Double price, int quantity,CallbackContext callbackContext){
        try {
            Screen screen = evergage.getScreenForActivity(this.cordova.getActivity());
            Product product = new Product(id);
            product.name = name;
            product.price = price;
            LineItem lineItem = new LineItem(product, quantity);
            if(screen != null)
                screen.addToCart(lineItem);
            else
                Objects.requireNonNull(evergage.getGlobalContext()).addToCart(lineItem);
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }

    private void trackAction(String event, CallbackContext callbackContext){
        try {
            Screen screen = evergage.getScreenForActivity(this.cordova.getActivity());
            if(screen != null)
                screen.trackAction(event);
            else
                Objects.requireNonNull(evergage.getGlobalContext()).trackAction(event);
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }

}

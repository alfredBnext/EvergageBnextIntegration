package mx.bnext.EvergageBnextIntegration;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import com.evergage.android.ClientConfiguration;
import com.evergage.android.Evergage;
import com.evergage.android.Screen;
import com.evergage.android.promote.Category;
import com.evergage.android.promote.LineItem;
import com.evergage.android.promote.Product;

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
        String id;
        String name;
        double price;
        switch (action){
            case "start":
                String account = args.getString(0);
                String dataset = args.getString(1);
                boolean usePushNotification = args.getBoolean(2);
                this.start(account, dataset, usePushNotification, callbackContext);
                return true;
            case "viewProduct":
                id = args.getString(0);
                name = args.getString(1);
                price = args.getDouble(2);
                this.viewProduct(id, name, price, callbackContext);
                return true;
            case "viewCategory":
                id = args.getString(0);
                name = args.getString(1);
                this.viewCategory(id, name, callbackContext);
                return true;
            case "addToCart":
                id = args.getString(0);
                name = args.getString(1);
                price = args.getDouble(2);
                int quantity = args.getInt(3);
                this.addToCart(id, name, price, quantity, callbackContext);
                return true;
            case "trackAction":
                String event = args.getString(0);
                this.trackAction(event, callbackContext);
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

    private void viewProduct(String id, String name, Double price, CallbackContext callbackContext){
        try {
            Screen screen = evergage.getScreenForActivity(this.cordova.getActivity());
            Product product = new Product(id);
            product.name = name;
            product.price = price;
            if(screen != null)
                screen.viewItem(product);
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
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }
}

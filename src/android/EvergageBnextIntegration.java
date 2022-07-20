package mx.bnext.EvergageBnextIntegration;

import android.util.Log;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import com.evergage.android.ClientConfiguration;
import com.evergage.android.Context;
import com.evergage.android.Evergage;
import com.evergage.android.LogLevel;
import com.evergage.android.Screen;
import com.evergage.android.promote.Category;
import com.evergage.android.promote.Item;
import com.evergage.android.promote.LineItem;
import com.evergage.android.promote.Order;
import com.evergage.android.promote.Product;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import mx.bnext.EvergageBnextIntegration.Models.ListSaleLine;
import mx.bnext.EvergageBnextIntegration.Models.SaleLine;

public class EvergageBnextIntegration extends CordovaPlugin {
    Evergage evergage;

    public List<String> actionsContemplated = new ArrayList<>(Arrays.asList(
            "start",
            "setUserId",
            "setLogLevel",
            "viewProduct",
            "viewCategory",
            "addToCart",
            "trackAction",
            "purchase"
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
        
        if (evergage.getGlobalContext() == null){
            callbackContext.error("Error en bundle id o url schema. Evergage is disabled");
            Log.i("Evergage", "Error en bundle id o url schema. Evergage is disabled");
            return;
        }

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
            case "setUserId":
                String userId = args.getString(0);
                String email = args.getString(1);
                String firstName = args.getString(2);
                String lastName = args.getString(3);
                this.setUserId(userId, email, firstName, lastName, callbackContext);
                break;
            case "viewProduct":
                id = args.getString(0);
                name = args.getString(1);;
                this.viewProduct(id, name, callbackContext);
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
            case "purchase":
                String orderId = args.getString(0);
                String lines = args.getString(1);
                double total = args.getDouble(2);
                this.purchase(orderId, lines, total, callbackContext);
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

    private void setUserId(String userId, String email, String firstName, String lastName, CallbackContext callbackContext){
        try{
            evergage.setUserId(userId);
            evergage.setUserAttribute("emailAddress", email);
            evergage.setUserAttribute("emailSHA256", sha256String(email));
            evergage.setUserAttribute("firstName", firstName);
            evergage.setUserAttribute("lastName", lastName);
            //evergage.setAccountAttribute();

            callbackContext.success("Ok");
        } catch (Exception e){
            callbackContext.error(e.toString());
        }
    }

    private void viewProduct(String id, String name, CallbackContext callbackContext){
        try {
            Screen screen = evergage.getScreenForActivity(this.cordova.getActivity());
            Context contextEvergage = evergage.getGlobalContext();
            Product product = new Product(id);
            product.name = name;
            if(screen != null)
                screen.viewItem(product);
            else
                Objects.requireNonNull(contextEvergage).viewItem(product);
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

    private void purchase(String orderId, String lines, double total, CallbackContext callbackContext){
        try {
            Gson gson = new Gson();
            ListSaleLine listSaleLine = gson.fromJson(lines, ListSaleLine.class);
            List<SaleLine> saleLines = listSaleLine.getList();
            List<LineItem> linesEvent = new ArrayList<>();
            Screen screen = evergage.getScreenForActivity(this.cordova.getActivity());

            for (SaleLine saleline: saleLines) {
                Product product = new Product(saleline.getId());
                product.name = saleline.getName();
                product.price = saleline.getPrice();
                linesEvent.add(new LineItem(product, saleline.getQuantity()));
            }

            if(screen != null)
                screen.purchase(new Order(orderId, linesEvent, total));
            else
                Objects.requireNonNull(evergage.getGlobalContext()).purchase(new Order(orderId, linesEvent, total));
            
            callbackContext.success("Ok");
        }catch (Exception e){
            callbackContext.error(e.toString());
        }
    }

    public String sha256String(String source) {
        byte[] hash = null;
        String hashCode = null;// w  ww  .  j  a va 2 s.c  o m
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(source.getBytes());
        } catch (NoSuchAlgorithmException e) {
            Log.d("Evergage", "Can't calculate SHA-256");
        }

        if (hash != null) {
            StringBuilder hashBuilder = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashBuilder.append("0");
                    hashBuilder.append(hex.charAt(hex.length() - 1));
                } else {
                    hashBuilder.append(hex.substring(hex.length() - 2));
                }
            }
            hashCode = hashBuilder.toString();
        }

        return hashCode;
    }
}

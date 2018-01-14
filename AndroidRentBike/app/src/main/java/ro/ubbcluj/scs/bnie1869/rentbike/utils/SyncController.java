package ro.ubbcluj.scs.bnie1869.rentbike.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;

/**
 * Created by nbodea on 1/13/2018.
 */

public class SyncController extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkChangeReceiver";
    private static WebSocketListener listener = null;
    private static WebSocket ws = null;
    public SyncController() {
        //SyncController.create_web_socket();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        changeNetworkStatus(context);
    }

    public void changeNetworkStatus(Context context) {
        System.out.println("Network change");
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = Globals.isAppConnected;
        boolean found = false;
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        Log.v(LOG_TAG, "Now you are connected to Internet!");
                        isConnected = true;
                        found = true;

                    }
                }
            }
        }
        if(!found) {
            isConnected = false;
            if(ws != null) {
                ws.close(1000, "No internet");
            }
        }

        System.out.println("Connected = " + Boolean.toString(isConnected));
        Globals.isAppConnected = isConnected;
        if(isConnected) {
            SyncController.create_web_socket();

            if(Globals.isListLoaded) {
                SyncController.merge();
            }
        }
    }

    public static void create_web_socket() {
        System.out.println("Creating ws");
        SyncController.listener = new WebSocketListener() {

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                System.out.println(text);
            }
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                System.out.println("MESSAGE: " + bytes.hex());
            }
        };



        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Globals.WS_SERVER_PATH)
                .build();
        ws = client.newWebSocket(request, SyncController.listener);

        client.dispatcher().executorService().shutdown();
    }

    public static void merge() {

    }

    public static void mergeResult(List<RentBikePlace> result) {

    }

    public static boolean elementCreated(RentBikePlace element) {

        return false;
    }

    public static void elementModified(RentBikePlace element, Boolean calledFromServer) {

    }

    public static void elementDeleted(RentBikePlace element) {

    }
}

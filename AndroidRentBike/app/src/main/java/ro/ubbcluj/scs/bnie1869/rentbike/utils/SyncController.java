package ro.ubbcluj.scs.bnie1869.rentbike.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
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
    private static OkHttpClient client = null;
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
                ws = null;
            }
        }

        System.out.println("Connected = " + Boolean.toString(isConnected));
        Globals.isAppConnected = isConnected;
        if(isConnected) {
            if(ws != null) {
                ws.close(1000, "Reconnecting...");
                ws = null;
            }
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
                try {
                    System.out.println(text);
                    super.onMessage(webSocket, text);
                    Gson gson = new Gson();
                    RentBikePlace element = gson.fromJson(text, RentBikePlace.class);
                    System.out.println(text);
                    if (element.state.compareTo("created") == 0) {
                        elementCreated(element, true);
                    } else if (element.state.compareTo("deleted") == 0) {
                        elementDeleted(element);
                    } else if (element.state.compareTo("edited") == 0) {
                        elementModified(element, true);
                    }
                }
                catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("CLOSING");
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                System.out.println("MESSAGE: " + bytes.hex());
            }

            };

        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Globals.WS_SERVER_PATH)
                .build();
        ws = client.newWebSocket(request, SyncController.listener);

        client.dispatcher().executorService().shutdown();
    }

    public static void merge() {
        RequestParams rp = new RequestParams();

        Gson gson = new Gson();

        String jsonList = gson.toJson(Globals.rentBikePlaceList);

        rp.add("list", jsonList);

        HttpCalls.post("/merge", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getString("status").compareTo("Success") != 0)
                    {
                        System.out.println("[Merge] error");
                        return;
                    }
                    String result = response.getString("result");
                    System.out.println(result);
                    Gson gson = new Gson();
                    RentBikePlace[] x = gson.fromJson(result, RentBikePlace[].class);
                    mergeResult(Arrays.asList(x));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void mergeResult(List<RentBikePlace> result) {
        int i;
        for(i=0; i<result.size(); i++) {
            if(result.get(i).getState().compareTo("created") == 0) {
                elementCreated(result.get(i), true);
            }
            else if(result.get(i).getState().compareTo("deleted") == 0) {
                elementDeleted(result.get(i));
            }
            else if(result.get(i).getState().compareTo("edited") == 0) {
                elementModified(result.get(i), true);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public static boolean elementCreated(final RentBikePlace element, Boolean calledFromServer) {

        if(calledFromServer) {
            element.setState("");
        }
        int i;
        boolean mustRet = false;
        for(i=0; i<Globals.rentBikePlaceList.size(); i++)
        {
            if(Globals.rentBikePlaceList.get(i).getAddress().compareTo(element.street) == 0) {
                if(calledFromServer)
                    Globals.rentBikePlaceList.get(i).setState("");
                mustRet = true;
            }
        }

        for(i=0; i<Globals.showRentBikePlaceList.size(); i++)
        {
            if(Globals.showRentBikePlaceList.get(i).getAddress().compareTo(element.street) == 0) {
                if(calledFromServer)
                    Globals.showRentBikePlaceList.get(i).setState("");
                mustRet = true;
            }
        }
        if(mustRet) {
            return false;
        }

        Globals.rentBikePlaceList.add(element);
        Globals.showRentBikePlaceList.add(element);
        // notify list if possible
        if(Globals.listView != null) {
            System.out.println("listview is NOT null");
            ((BaseAdapter) Globals.listView.getAdapter()).notifyDataSetChanged();
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Globals.localStorage.add(element);
                return null;
            }
        }.execute();

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public static void elementModified(final RentBikePlace element, Boolean calledFromServer) {
        if(calledFromServer) {
            element.setState("");
        }
        int i;
        for(i=0; i<Globals.rentBikePlaceList.size(); i++) {
            if(Globals.rentBikePlaceList.get(i).street.compareTo(element.street) == 0) {
                Globals.rentBikePlaceList.get(i).setState(element.state);
                Globals.rentBikePlaceList.get(i).setNumberOfAvailableBikes(element.available);
                Globals.rentBikePlaceList.get(i).setNumberOfBikes(element.total);
                Globals.rentBikePlaceList.get(i).setActive(element.active);
            }
        }

        for(i = 0; i<Globals.showRentBikePlaceList.size(); i++) {
            if(Globals.showRentBikePlaceList.get(i).street.compareTo(element.street) == 0) {
                Globals.showRentBikePlaceList.get(i).setState(element.state);
                Globals.showRentBikePlaceList.get(i).setNumberOfAvailableBikes(element.available);
                Globals.showRentBikePlaceList.get(i).setNumberOfBikes(element.total);
                Globals.showRentBikePlaceList.get(i).setActive(element.active);
            }
        }

        for(i=0; i<Globals.showRentBikePlaceList.size(); i++) {
            if(Globals.showRentBikePlaceList.get(i).state.compareTo("deleted") == 0) {
                Globals.showRentBikePlaceList.remove(i);
            }
        }

        // notify list if possible
        if(Globals.listView != null) {
            System.out.println("listview is NOT null");
            ((BaseAdapter) Globals.listView.getAdapter()).notifyDataSetChanged();
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Globals.localStorage.update(element);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public static void elementDeleted(final RentBikePlace element) {
        int i;
        for(i=0; i<Globals.rentBikePlaceList.size(); i++) {
            if(Globals.rentBikePlaceList.get(i).street.compareTo(element.street) == 0) {
                Globals.rentBikePlaceList.remove(i);
            }
        }


        for(i=0; i<Globals.showRentBikePlaceList.size(); i++) {
            if(Globals.showRentBikePlaceList.get(i).street.compareTo(element.street) == 0) {
                Globals.showRentBikePlaceList.remove(i);
            }
        }

        // notify list if possible
        if(Globals.listView != null) {
            System.out.println("listview is NOT null");
            ((BaseAdapter) Globals.listView.getAdapter()).notifyDataSetChanged();
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Globals.localStorage.delete(element);
                return null;
            }
        }.execute();

    }
}

package ro.ubbcluj.scs.bnie1869.scheletexamen.ws;

import android.app.NotificationManager;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import ro.ubbcluj.scs.bnie1869.scheletexamen.MainActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;

/**
 * Created by nbodea on 2/2/2018.
 */

public class CarListener {
    private String WS_SERVER_PATH = "http://192.168.0.157:4000";
    private WebSocket ws;
    public CarListener() {
        Request request = new Request.Builder().url(WS_SERVER_PATH).build();
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(text);
                    Car car =  new Car(jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getInt("quantity"),
                            jsonObject.getString("type"),
                            jsonObject.getString("status"));
                    MainActivity.clientService.doAddNotification(car);
                    MainActivity.employeeService.doAddNotification(car);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("CONN CLOSED");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                System.out.println("FAILURE");
                System.out.println(t.getMessage());
                t.printStackTrace();
            }
        };
        OkHttpClient client =new OkHttpClient.Builder().build();

        ws = client.newWebSocket(request, listener);
    }

    public void uninit() {
        ws.cancel();
    }

}

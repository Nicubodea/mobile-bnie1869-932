package ro.ubbcluj.scs.bnie1869.rentbike.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import ro.ubbcluj.scs.bnie1869.rentbike.model.Token;
import ro.ubbcluj.scs.bnie1869.rentbike.model.User;

/**
 * Created by nbodea on 1/13/2018.
 */

public class HttpCalls {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(Globals.SERVER_PATH + url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(Globals.SERVER_PATH + url, params, responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(Globals.SERVER_PATH + url, params, responseHandler);
    }

    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(Globals.SERVER_PATH + url, params, responseHandler);
    }
}

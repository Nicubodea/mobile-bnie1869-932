package ro.ubbcluj.scs.bnie1869.rentbike.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;

/**
 * Created by nbodea on 1/13/2018.
 */

public class SyncController extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        changeNetworkStatus(context);
    }

    public void changeNetworkStatus(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = Globals.isAppConnected;
        boolean found = false;
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!Globals.isAppConnected) {
                            Log.v(LOG_TAG, "Now you are connected to Internet!");
                            isConnected = true;
                            found = true;
                        }
                    }
                }
            }
        }
        if(!found) {
            isConnected = false;
        }

        if(isConnected && !Globals.isAppConnected) {
            SyncController.create_web_socket();

            if(Globals.isListLoaded) {
                SyncController.merge();
            }
        }
    }

    public static void create_web_socket() {

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

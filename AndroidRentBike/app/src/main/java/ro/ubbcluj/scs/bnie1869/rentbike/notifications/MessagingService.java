package ro.ubbcluj.scs.bnie1869.rentbike.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ro.ubbcluj.scs.bnie1869.rentbike.MainActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.R;

/**
 * Created by nbodea on 1/14/2018.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


            handleNow();

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

}
package ro.ubbcluj.scs.bnie1869.rentbike;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cz.msebera.android.httpclient.Header;
import ro.ubbcluj.scs.bnie1869.rentbike.admin.ContactActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.common.LoginActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.model.Token;
import ro.ubbcluj.scs.bnie1869.rentbike.model.User;
import ro.ubbcluj.scs.bnie1869.rentbike.user.UserContactActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.Globals;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.HttpCalls;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.LocalStorage;
import ro.ubbcluj.scs.bnie1869.rentbike.db.RentBikePlaceDB;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.SyncController;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private void init_globals() {
        Globals.databaseSingleton = Room.databaseBuilder(getApplicationContext(), RentBikePlaceDB.class, "dummy-database").build();
        Globals.localStorage = new LocalStorage(Globals.databaseSingleton);
        Globals.isAppConnected = true;
        Globals.isListLoaded = false;
        Globals.isLoggedIn = false;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Globals.rentBikePlaceList = Globals.localStorage.getList();
                Globals.showRentBikePlaceList = new ArrayList<>();

                for(int i = 0; i<Globals.rentBikePlaceList.size(); i++) {
                    if(Globals.rentBikePlaceList.get(i).state.compareTo("deleted") != 0) {
                        Globals.showRentBikePlaceList.add(Globals.rentBikePlaceList.get(i));
                    }
                }
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    private void check_login_status_and_redirect() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Globals.token = Globals.localStorage.getToken();
                return null;
            }

            protected void onPostExecute(Void result) {
                if (Globals.token == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    //inish();
                } else {
                    RequestParams rp = new RequestParams();
                    rp.add("token", Globals.token.token);
                    HttpCalls.post("/get_my_user", rp, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    try {
                                        JSONObject serverResp = new JSONObject(response.toString());
                                        if (serverResp.getString("status").compareTo("Success") != 0) {
                                            System.out.println("Eroare");
                                            return;
                                        }

                                        JSONObject user = new JSONObject(serverResp.getString("user"));
                                        if (user.getInt("role") == 0) {
                                            startActivity(new Intent(MainActivity.this, UserContactActivity.class));
                                            //finish();
                                        } else if (user.getInt("role") == 1) {
                                            startActivity(new Intent(MainActivity.this, ContactActivity.class));
                                            //finish();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
                }
            }

        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Globals.SyncController = new SyncController();
        registerReceiver(
                Globals.SyncController,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));

        init_globals();
        check_login_status_and_redirect();

    }

    protected void onStop() {
        super.onStop();
    }
}

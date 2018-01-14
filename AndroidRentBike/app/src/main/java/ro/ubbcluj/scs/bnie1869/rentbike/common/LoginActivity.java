package ro.ubbcluj.scs.bnie1869.rentbike.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import ro.ubbcluj.scs.bnie1869.rentbike.MainActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.R;
import ro.ubbcluj.scs.bnie1869.rentbike.admin.ContactActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.model.Token;
import ro.ubbcluj.scs.bnie1869.rentbike.user.UserContactActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.Globals;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.HttpCalls;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button lb = findViewById(R.id.login_button);

        new AsyncTask<Void, Void, Void> () {

            @Override
            protected Void doInBackground(Void... voids) {
                Globals.localStorage.deleteToken();
                return null;
            }
        }.execute();

        lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText)findViewById(R.id.textbox_user)).getText().toString();
                String password = ((EditText)findViewById(R.id.textbox_pass)).getText().toString();

                login(username, password);

            }
        });
    }

    protected void login(String username, String password) {
        RequestParams rp = new RequestParams();
        rp.add("username", username);
        rp.add("password", password);
        System.out.println("LOGIN");
        HttpCalls.post("/login", rp, new JsonHttpResponseHandler() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                        try {
                            JSONObject serverResp = new JSONObject(response.toString());
                            if(serverResp.getString("status").compareTo("Success") != 0)
                            {
                                System.out.println("Error");
                                return;
                            }

                            String token = serverResp.getString("token");
                            Globals.token = new Token();
                            Globals.token.token = token;

                            RequestParams rp = new RequestParams();
                            rp.add("token", Globals.token.token);

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    Globals.localStorage.newToken(Globals.token.token);
                                    return null;
                                }
                            }.execute();

                            System.out.println("GET_MY_USER");
                            HttpCalls.post("/get_my_user", rp, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            // If the response is JSONObject instead of expected JSONArray
                                            try {
                                                JSONObject serverResp = new JSONObject(response.toString());
                                                if(serverResp.getString("status").compareTo("Success") != 0)
                                                {
                                                    System.out.println("Error");
                                                    // signal error
                                                    return;
                                                }

                                                JSONObject user = new JSONObject(serverResp.getString("user"));
                                                if(user.getInt("role") == 0) {
                                                    startActivity(new Intent(LoginActivity.this, UserContactActivity.class));
                                                    finish();
                                                }
                                                else if(user.getInt("role") == 1) {
                                                    startActivity(new Intent(LoginActivity.this, ContactActivity.class));
                                                    finish();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }
                            );


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
        );
    }
}

package ro.ubbcluj.scs.bnie1869.scheletexamen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;

public class ClientActivity extends AppCompatActivity {

    ListView clientList;
    public static ClientActivity instance;

    Button refresh;
    Button viewPurchased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);



        clientList = findViewById(R.id.listviewclient);
        clientList.setAdapter(MainActivity.clientService.getClientListAdapter());
        instance = this;

        safeGetAll();

        refresh = findViewById(R.id.button);
        viewPurchased = findViewById(R.id.button2);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safeGetAll();
            }
        });

        viewPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientActivity.this, PurchasedCarsActivity.class));
            }
        });

    }

    private void safeGetAll() {
        boolean network = MainActivity.networkConnectivity(MainActivity.clientService.getClientListAdapter().getAppContext());
        if(!network)
        {
            Snackbar.make(ClientActivity.instance.findViewById(R.id.listviewclient), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            safeGetAll();
                        }
                    }).show();
        }
        else {
            MainActivity.clientService.getAll(ClientActivity.this);
        }
    }

}

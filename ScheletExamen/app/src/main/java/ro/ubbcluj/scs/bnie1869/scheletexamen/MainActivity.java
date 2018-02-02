package ro.ubbcluj.scs.bnie1869.scheletexamen;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ro.ubbcluj.scs.bnie1869.scheletexamen.adapters.ClientListAdapter;
import ro.ubbcluj.scs.bnie1869.scheletexamen.adapters.EmployeeListAdapter;
import ro.ubbcluj.scs.bnie1869.scheletexamen.adapters.PurchasedListAdapter;
import ro.ubbcluj.scs.bnie1869.scheletexamen.db.CarDb;
import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;
import ro.ubbcluj.scs.bnie1869.scheletexamen.service.ClientService;
import ro.ubbcluj.scs.bnie1869.scheletexamen.service.EmployeeService;
import ro.ubbcluj.scs.bnie1869.scheletexamen.ws.CarListener;

public class MainActivity extends AppCompatActivity {

    Button clientButton;
    Button employeeButton;

    public static ClientService clientService = new ClientService(new ArrayList<Car>(), new ArrayList<Car>());
    public static EmployeeService employeeService = new EmployeeService(new ArrayList<Car>());
    public ClientListAdapter clientListAdapter;
    public EmployeeListAdapter employeeListAdapter;
    public PurchasedListAdapter purchasedListAdapter;
    public static CarDb database;
    public static CarListener carListener = new CarListener();

    public void initAttributes() {
        clientButton = findViewById(R.id.button3);
        employeeButton = findViewById(R.id.button4);

        clientListAdapter = new ClientListAdapter(getApplicationContext(), clientService);
        employeeListAdapter = new EmployeeListAdapter(getApplicationContext(), employeeService);
        purchasedListAdapter = new PurchasedListAdapter(getApplicationContext(), clientService);

        clientService.setClientListAdapter(clientListAdapter);
        clientService.setPurchasedListAdapter(purchasedListAdapter);

        employeeService.setEmployeeListAdapter(employeeListAdapter);

        database = CarDb.getAppDatabase(getApplicationContext());
    }

    public static boolean networkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAttributes();

        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToClient();
            }
        });

        employeeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goToEmployee();
            }
        });


    }

    protected void goToClient() {
        startActivity(new Intent(MainActivity.this, ClientActivity.class));
    }

    protected void goToEmployee() {
        startActivity(new Intent(MainActivity.this, EmployeeActivity.class));
    }
}

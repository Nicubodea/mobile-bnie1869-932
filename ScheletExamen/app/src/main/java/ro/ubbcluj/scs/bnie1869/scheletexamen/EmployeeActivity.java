package ro.ubbcluj.scs.bnie1869.scheletexamen;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class EmployeeActivity extends AppCompatActivity {

    public static EmployeeActivity instance;
    ListView list;
    Button refresh;
    Button addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        instance = this;
        list = findViewById(R.id.listviewemployee);
        list.setAdapter(MainActivity.employeeService.getEmployeeListAdapter());

        safeGetAll();

        refresh = findViewById(R.id.button5);
        addNew = findViewById(R.id.button6);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safeGetAll();
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmployeeActivity.this, AddCarActivity.class));
            }
        });
    }

    private void safeGetAll() {
        boolean network = MainActivity.networkConnectivity(MainActivity.clientService.getClientListAdapter().getAppContext());
        if(!network)
        {
            Snackbar.make(EmployeeActivity.instance.findViewById(R.id.listviewemployee), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            safeGetAll();
                        }
                    }).show();
        }
        else {
            MainActivity.employeeService.getAll(EmployeeActivity.this);
        }
    }

}

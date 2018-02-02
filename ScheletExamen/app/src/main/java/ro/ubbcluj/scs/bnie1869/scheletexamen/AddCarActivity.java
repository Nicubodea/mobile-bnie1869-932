package ro.ubbcluj.scs.bnie1869.scheletexamen;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;

public class AddCarActivity extends AppCompatActivity {

    EditText name;
    EditText type;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        name = findViewById(R.id.editText);
        type = findViewById(R.id.editText3);

        button = findViewById(R.id.button8);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safeAdd();
            }
        });

    }

    private void safeAdd() {
        boolean network = MainActivity.networkConnectivity(MainActivity.clientService.getClientListAdapter().getAppContext());
        if(!network)
        {
            Snackbar.make(AddCarActivity.this.findViewById(R.id.button8), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            safeAdd();
                        }
                    }).show();
        }
        else {
            Car car = new Car(0, name.getText().toString(), 0, type.getText().toString(), "");
            MainActivity.employeeService.doAdd(AddCarActivity.this, car);
        }
    }

}

package ro.ubbcluj.scs.bnie1869.scheletexamen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class PurchasedCarsActivity extends AppCompatActivity {

    public static PurchasedCarsActivity instance;
    ListView purchased;
    Button delButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_cars);

        instance = this;

        MainActivity.clientService.getAllPurchased();

        purchased = findViewById(R.id.listviewpurchased);
        purchased.setAdapter(MainActivity.clientService.getPurchasedListAdapter());

        delButton = findViewById(R.id.button7);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.clientService.clearPurchased();
            }
        });

    }

}

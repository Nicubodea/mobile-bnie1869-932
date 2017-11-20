package ro.ubbcluj.scs.bnie1869.rentbike;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TabLayout layout = findViewById(R.id.detailsTabs);

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                finish();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                finish();
            }
        });

        LinearLayout layout1 = findViewById(R.id.detailslayout);

        Intent intent = getIntent();
        final String currentAddress = intent.getStringExtra("address");
        TextView textView = new TextView(this);
        textView.setText(currentAddress);
        textView.setTextSize((float)20);

        layout1.addView(textView);

        RentBikePlace rentBikePlace = getRentBikePlace(currentAddress);

        final EditText editTextNumberOfBikes = new EditText(this);
        editTextNumberOfBikes.setText(rentBikePlace.getNumberOfBikes().toString());

        final EditText editTextNumberOfAvailable = new EditText(this);
        editTextNumberOfAvailable.setText(rentBikePlace.getNumberOfAvailableBikes().toString());

        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Integer nrOfBikes = Integer.parseInt(editTextNumberOfBikes.getText().toString());
                    Integer nrOfAv = Integer.parseInt(editTextNumberOfAvailable.getText().toString());

                    if (nrOfAv > nrOfBikes) {
                        Toast.makeText(DetailsActivity.this, "Can't have more available bikes than bikes!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    setRentBikePlace(currentAddress, nrOfBikes, nrOfAv);
                    Toast.makeText(DetailsActivity.this, "Edited succesfully!", Toast.LENGTH_LONG).show();

                } catch(RuntimeException e) {
                    Toast.makeText(DetailsActivity.this, "Error at input numbers!", Toast.LENGTH_LONG).show();
                }

            }
        });

        layout1.addView(editTextNumberOfBikes);
        layout1.addView(editTextNumberOfAvailable);
        layout1.addView(editButton);


    }


    RentBikePlace getRentBikePlace(String address) {
        int i;
        for(i=0; i<MainActivity.listOfBikes.size(); i++) {
            if(address.compareTo(MainActivity.listOfBikes.get(i).address) == 0) {
                return MainActivity.listOfBikes.get(i);
            }
        }
        return null;
    }

    void setRentBikePlace(String address, Integer nrBikes, Integer nrAvailable) {
        int i;
        for(i=0; i<MainActivity.listOfBikes.size(); i++) {
            if(address.compareTo(MainActivity.listOfBikes.get(i).address) == 0) {
                MainActivity.listOfBikes.get(i).setNumberOfBikes(nrBikes);
                MainActivity.listOfBikes.get(i).setNumberOfAvailableBikes(nrAvailable);
            }
        }
    }


}

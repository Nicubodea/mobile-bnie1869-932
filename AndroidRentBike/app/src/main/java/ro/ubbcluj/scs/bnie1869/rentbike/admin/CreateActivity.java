package ro.ubbcluj.scs.bnie1869.rentbike.admin;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import ro.ubbcluj.scs.bnie1869.rentbike.R;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.SyncController;

public class CreateActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private final static String TAG = "CreateActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        TabLayout layout = findViewById(R.id.createTabs);

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

        final NumberPicker editTextNumberOfBikes = findViewById(R.id.createTotal);
        editTextNumberOfBikes.setMaxValue(1000);
        editTextNumberOfBikes.setMinValue(0);

        NumberPicker editTextNumberOfAvailable = findViewById(R.id.createAvailable);
        editTextNumberOfAvailable.setMinValue(0);
        editTextNumberOfAvailable.setMaxValue(1000);

        editTextNumberOfAvailable.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //Log.i(TAG, "[NUMBER OF AVAILABLE] User picked: " + Integer.toString(i) + "; " + Integer.toString(i1));
                numberPicker.setValue(i1);
            }
        });

        editTextNumberOfBikes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                //Log.i(TAG, "[TOTAL] User picked: " + Integer.toString(i) + "; " + Integer.toString(i1));
                numberPicker.setValue(i1);
            }
        });

        Button createButton = findViewById(R.id.createFinish);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer total = ((NumberPicker)findViewById(R.id.createTotal)).getValue();
                final Integer avail = ((NumberPicker)findViewById(R.id.createAvailable)).getValue();
                final String street = ((EditText)findViewById(R.id.createStreet)).getText().toString();

                if(avail > total)
                {
                    Toast.makeText(CreateActivity.this, "Can't have more available bikes than bikes!", Toast.LENGTH_LONG).show();
                    return;
                }
/*
                if(!createNewRentBikePlace(street, total, avail))
                {
                    Toast.makeText(CreateActivity.this, "Street exists already", Toast.LENGTH_LONG).show();
                    return;
                }
                ((BaseAdapter)ViewListActivity2.staticMyList.getAdapter()).notifyDataSetChanged();

                // we must sync our data with the server and local storage


                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ContactActivity.localStorage.add(new RentBikePlace(street, total, avail, "undefined"));
                        return null;
                    }
                }.execute();

*/

                if(!SyncController.elementCreated(new RentBikePlace(street, total, avail, "created"))) {

                }
                Toast.makeText(CreateActivity.this, "Created succesfully!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });


    }

        /*
    public boolean createNewRentBikePlace(String street, int total, int avail)
    {
        int i;
        for(i=0; i< ContactActivity.listOfBikes.size(); i++) {
            if (street.compareTo(ContactActivity.listOfBikes.get(i).address) == 0) {
                return false;
            }
        }

        ContactActivity.listOfBikes.add(new RentBikePlace(street, total, avail, "undefined"));
        return true;

    }
    */
}

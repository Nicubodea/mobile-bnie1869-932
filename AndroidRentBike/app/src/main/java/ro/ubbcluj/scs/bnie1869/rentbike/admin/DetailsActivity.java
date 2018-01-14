package ro.ubbcluj.scs.bnie1869.rentbike.admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import ro.ubbcluj.scs.bnie1869.rentbike.R;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.Globals;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.HttpCalls;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.SyncController;

public class DetailsActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private static final String TAG = "DetailsActivity";

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

        Intent intent = getIntent();
        final String currentAddress = intent.getStringExtra("address");
        TextView textView = findViewById(R.id.streetText);
        textView.setText(currentAddress);
        textView.setTextSize((float)20);

        RentBikePlace rentBikePlace = getRentBikePlace(currentAddress);

        NumberPicker editTextNumberOfBikes = findViewById(R.id.pickerTotal);
        editTextNumberOfBikes.setMaxValue(1000);
        editTextNumberOfBikes.setMinValue(0);
        editTextNumberOfBikes.setValue(rentBikePlace.getNumberOfBikes());

        NumberPicker editTextNumberOfAvailable = findViewById(R.id.pickerAvailable);
        editTextNumberOfAvailable.setMinValue(0);
        editTextNumberOfAvailable.setMaxValue(1000);
        editTextNumberOfAvailable.setValue(rentBikePlace.getNumberOfAvailableBikes());

        editTextNumberOfAvailable.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.i(TAG, "[NUMBER OF AVAILABLE] User picked: " + Integer.toString(i) + "; " + Integer.toString(i1));
                numberPicker.setValue(i1);
            }
        });

        editTextNumberOfBikes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                Log.i(TAG, "[TOTAL] User picked: " + Integer.toString(i) + "; " + Integer.toString(i1));
                numberPicker.setValue(i1);
            }
        });

        Button editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    final Integer nrOfBikes = ((NumberPicker)findViewById(R.id.pickerTotal)).getValue();
                    final Integer nrOfAv = ((NumberPicker)findViewById(R.id.pickerAvailable)).getValue();

                    if (nrOfAv > nrOfBikes) {
                        Toast.makeText(DetailsActivity.this, "Can't have more available bikes than bikes!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    RequestParams rp = new RequestParams();
                    rp.add("token", Globals.token.token);
                    rp.add("street", currentAddress);
                    rp.add("total", nrOfBikes.toString());
                    rp.add("available", nrOfAv.toString());
                    rp.add("active", "Active");

                    HttpCalls.put("/edit_rbp", rp, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                if(response.getString("status").compareTo("Success") != 0) {
                                    System.out.println(response.getString("reason"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    SyncController.elementModified(new RentBikePlace(currentAddress, nrOfBikes, nrOfAv, "edited"), false);

                    Toast.makeText(DetailsActivity.this, "Edited succesfully!", Toast.LENGTH_LONG).show();
                    onBackPressed();

                } catch(RuntimeException e) {
                    Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });


        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setMessage("Are you sure you want to delete this entry? Deletion is irreversible!");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    public void onClick(DialogInterface dialog, int id) {


                        RentBikePlace oldRentBikePlace = DetailsActivity.this.getRentBikePlace(currentAddress);

                        final RentBikePlace savedRentBikePlace = new RentBikePlace(oldRentBikePlace.getAddress(), oldRentBikePlace.getNumberOfBikes(), oldRentBikePlace.getNumberOfAvailableBikes(), "deleted");

                        RequestParams rp = new RequestParams();
                        rp.add("token", Globals.token.token);
                        rp.add("street", savedRentBikePlace.street);

                        System.out.println(Globals.token.token);
                        System.out.println(savedRentBikePlace.street);

                        HttpCalls.delete("/delete_rbp", rp, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    if(response.getString("status").compareTo("Success") != 0) {
                                        System.out.println(response.getString("reason"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        SyncController.elementModified(savedRentBikePlace, false);

                        Toast.makeText(DetailsActivity.this, "Deleted succesfully!", Toast.LENGTH_LONG).show();
                        onBackPressed();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "User canceled deletion");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }));

        PieChart pieChart = findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        // add values into pie chart data set
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        Integer v1 = editTextNumberOfAvailable.getValue();
        Integer v2 = editTextNumberOfBikes.getValue();

        yvalues.add(new PieEntry((float)(v1)/(float)(v2), 0));
        yvalues.add(new PieEntry((float)(v2-v1)/(float)(v2), 1));

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.rgb("009900")); // green for available bikes
        colors.add(ColorTemplate.rgb("990000")); // red for unavailable bikes
        dataSet.setColors(colors);

        // make pie data
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());

        // piechart legend
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);

        pieChart.setData(data);


    }

    RentBikePlace getRentBikePlace(String address) {
        int i;
        for(i=0; i< Globals.showRentBikePlaceList.size(); i++) {
            if(address.compareTo(Globals.showRentBikePlaceList.get(i).street) == 0) {
                return Globals.showRentBikePlaceList.get(i);
            }
        }
        return null;
    }

    /*
    void deleteRentBikePlace(String address) {

        int i;
        for(i=0; i< ContactActivity.listOfBikes.size(); i++) {
            if(address.compareTo(ContactActivity.listOfBikes.get(i).address) == 0) {
                ContactActivity.listOfBikes.remove(i);
                break;
            }
        }
    }

    void setRentBikePlace(String address, Integer nrBikes, Integer nrAvailable) {
        int i;
        for(i=0; i< ContactActivity.listOfBikes.size(); i++) {
            if(address.compareTo(ContactActivity.listOfBikes.get(i).address) == 0) {
                ContactActivity.listOfBikes.get(i).setNumberOfBikes(nrBikes);
                ContactActivity.listOfBikes.get(i).setNumberOfAvailableBikes(nrAvailable);
            }
        }
    }
    */

}

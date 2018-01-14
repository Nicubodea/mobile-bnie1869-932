package ro.ubbcluj.scs.bnie1869.rentbike.user;

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

import java.util.ArrayList;

import ro.ubbcluj.scs.bnie1869.rentbike.R;
import ro.ubbcluj.scs.bnie1869.rentbike.admin.DetailsActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.Globals;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.SyncController;

public class UserDetailsActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        TabLayout layout = findViewById(R.id.user_detailsTabs);

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
        TextView textView = findViewById(R.id.user_streetText);
        textView.setText(currentAddress);
        textView.setTextSize((float)20);

        RentBikePlace rentBikePlace = getRentBikePlace(currentAddress);

        NumberPicker editTextNumberOfBikes = findViewById(R.id.user_pickerTotal);
        editTextNumberOfBikes.setMaxValue(1000);
        editTextNumberOfBikes.setMinValue(0);
        editTextNumberOfBikes.setValue(rentBikePlace.getNumberOfBikes());
        editTextNumberOfBikes.setEnabled(false);

        NumberPicker editTextNumberOfAvailable = findViewById(R.id.user_pickerAvailable);
        editTextNumberOfAvailable.setMinValue(0);
        editTextNumberOfAvailable.setMaxValue(1000);
        editTextNumberOfAvailable.setValue(rentBikePlace.getNumberOfAvailableBikes());
        editTextNumberOfAvailable.setEnabled(false);


        PieChart pieChart = findViewById(R.id.user_piechart);

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
}

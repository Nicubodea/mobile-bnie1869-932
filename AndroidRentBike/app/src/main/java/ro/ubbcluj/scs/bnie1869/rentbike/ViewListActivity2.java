package ro.ubbcluj.scs.bnie1869.rentbike;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewListActivity2 extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list2);

        TabLayout layout = findViewById(R.id.viewListTabs);
        layout.getTabAt(1).select();

        populateBikeList();

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    finish();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    void populateBikeList() {
        List<RentBikePlace> currentList = getBikeList();

        int i;
        LinearLayout listLayout = findViewById(R.id.listlayout);

        for(i=0; i< currentList.size(); i++)
        {
            Button y = new Button(this);
            y.setText(currentList.get(i).toString());
            y.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickGoToDetails(view);
                }
            });

            listLayout.addView(y);


        }
    }


    List<RentBikePlace> getBikeList() {
       return MainActivity.listOfBikes;

    }


    void onClickGoToDetails(View v) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("address", ((Button) v).getText());
        startActivity(intent);
    }


}

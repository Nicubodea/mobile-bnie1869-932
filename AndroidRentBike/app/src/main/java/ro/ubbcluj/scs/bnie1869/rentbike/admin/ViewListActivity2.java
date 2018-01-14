package ro.ubbcluj.scs.bnie1869.rentbike.admin;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.rentbike.R;
import ro.ubbcluj.scs.bnie1869.rentbike.common.LoginActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.Globals;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;

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

        Globals.listView = findViewById(R.id.mylist);
        TabLayout layout = findViewById(R.id.viewListTabs);
        layout.getTabAt(1).select();

        populateBikeList();

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    startActivity(new Intent(ViewListActivity2.this, ContactActivity.class));
                    finish();
                }
                if(tab.getPosition() == 2) {
                    startActivity(new Intent(ViewListActivity2.this, LoginActivity.class));
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

        Button createButton = findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewListActivity2.this, CreateActivity.class));
            }
        });
    }


    void populateBikeList() {

        List<RentBikePlace> currentList = Globals.showRentBikePlaceList;

        ListView listView = findViewById(R.id.mylist);

        listView.setAdapter(new ArrayAdapter<RentBikePlace>(this, android.R.layout.simple_list_item_1, Globals.showRentBikePlaceList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                RentBikePlace item = (RentBikePlace) parent.getItemAtPosition(position);
                String address = item.getAddress();

                Intent intent;
                intent = new Intent(ViewListActivity2.this, DetailsActivity.class);
                intent.putExtra("address", address);
                startActivity(intent);
            }

        });

    }

}

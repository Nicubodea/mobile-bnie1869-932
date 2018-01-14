package ro.ubbcluj.scs.bnie1869.rentbike.user;

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
import ro.ubbcluj.scs.bnie1869.rentbike.admin.ContactActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.admin.CreateActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.admin.DetailsActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.admin.ViewListActivity2;
import ro.ubbcluj.scs.bnie1869.rentbike.common.LoginActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.Globals;

public class UserViewListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_list);


        Globals.listView = findViewById(R.id.user_mylist);
        TabLayout layout = findViewById(R.id.user_viewListTabs);
        layout.getTabAt(1).select();

        populateBikeList();

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    startActivity(new Intent(UserViewListActivity.this, UserContactActivity.class));
                    finish();
                }
                if(tab.getPosition() == 2) {
                    startActivity(new Intent(UserViewListActivity.this, LoginActivity.class));
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

        List<RentBikePlace> currentList = Globals.showRentBikePlaceList;

        ListView listView = findViewById(R.id.user_mylist);

        listView.setAdapter(new ArrayAdapter<RentBikePlace>(this, android.R.layout.simple_list_item_1, Globals.showRentBikePlaceList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                RentBikePlace item = (RentBikePlace) parent.getItemAtPosition(position);
                String address = item.getAddress();

                Intent intent;
                intent = new Intent(UserViewListActivity.this, UserDetailsActivity.class);
                intent.putExtra("address", address);
                startActivity(intent);
            }

        });

    }
}

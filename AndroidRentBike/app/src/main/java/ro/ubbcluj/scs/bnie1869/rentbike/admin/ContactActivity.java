package ro.ubbcluj.scs.bnie1869.rentbike.admin;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.rentbike.common.LoginActivity;
import ro.ubbcluj.scs.bnie1869.rentbike.utils.LocalStorage;
import ro.ubbcluj.scs.bnie1869.rentbike.R;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.db.RentBikePlaceDB;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        final TabLayout layout = findViewById(R.id.mainTabs);

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1) {
                    startActivity(new Intent(ContactActivity.this, ViewListActivity2.class));
                    finish();
                }
                if(tab.getPosition() == 2) {
                    startActivity(new Intent(ContactActivity.this, LoginActivity.class));
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

    public void onSendClick(View v) {

        TextView subjectView = findViewById(R.id.editText3);
        TextView messageView = findViewById(R.id.editText2);

        String subject = subjectView.getText().toString();
        String message = messageView.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);


        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nicubodea96@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

}

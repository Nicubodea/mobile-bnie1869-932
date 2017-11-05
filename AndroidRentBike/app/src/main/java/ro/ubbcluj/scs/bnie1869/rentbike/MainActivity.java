package ro.ubbcluj.scs.bnie1869.rentbike;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Boolean bSubjectChanged = false;
    static List<RentBikePlace> listOfBikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabLayout layout = findViewById(R.id.mainTabs);

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1) {
                    layout.getTabAt(0).select();
                    startActivity(new Intent(MainActivity.this, ViewListActivity2.class));

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        MainActivity.listOfBikes = new ArrayList<>();
        MainActivity.listOfBikes.add(new RentBikePlace("Strada Fabricii nr. 16", 21, 10));
        MainActivity.listOfBikes.add(new RentBikePlace("Strada 21 Decembrie 1989", 44, 15));
        MainActivity.listOfBikes.add(new RentBikePlace("Strada Alunelor nr. 44", 66, 33));


    }

    void onSendClick(View v) {

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

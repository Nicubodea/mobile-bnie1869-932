package ro.ubbcluj.scs.bnie1869.scheletexamen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.scheletexamen.ClientActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.EmployeeActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.MainActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.PurchasedCarsActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.R;
import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;
import ro.ubbcluj.scs.bnie1869.scheletexamen.service.ClientService;

/**
 * Created by nbodea on 2/2/2018.
 */

public class ClientListAdapter extends BaseAdapter {

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    private List<Car> carList;

    public Context getAppContext() {
        return appContext;
    }

    private Context appContext;
    private ClientService service;

    public ClientListAdapter(Context appContext, ClientService service) {
        this.carList = service.getList();
        this.appContext = appContext;
        this.service = service;
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Object getItem(int i) {
        return carList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return carList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") final View v = View.inflate(this.appContext, R.layout.view_car_client, null);

        TextView name = v.findViewById(R.id.textView5);
        TextView quantity = v.findViewById(R.id.textView6);
        TextView type = v.findViewById(R.id.textView7);

        Button buybutton = v.findViewById(R.id.button9);

        final Car current = carList.get(i);

        name.setText(current.getName());
        quantity.setText(current.getQuantity().toString());
        type.setText(current.getType());

        buybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safeBuy(v, current);

            }
        });
        return v;

    }

    private void safeBuy(final View v, final Car current) {
        boolean network = MainActivity.networkConnectivity(MainActivity.clientService.getClientListAdapter().getAppContext());
        if(!network)
        {
            Snackbar.make(ClientActivity.instance.findViewById(R.id.listviewclient), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            safeBuy(v, current);
                        }
                    }).show();
        }
        else {
            EditText qView = v.findViewById(R.id.editText4);
            service.doBuy(ClientActivity.instance, current, qView.getText().toString());
        }
    }
}

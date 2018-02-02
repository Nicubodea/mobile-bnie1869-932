package ro.ubbcluj.scs.bnie1869.scheletexamen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.scheletexamen.EmployeeActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.MainActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.PurchasedCarsActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.R;
import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;
import ro.ubbcluj.scs.bnie1869.scheletexamen.service.ClientService;

/**
 * Created by nbodea on 2/2/2018.
 */

public class PurchasedListAdapter extends BaseAdapter {

    private ClientService service;

    public Context getAppContext() {
        return appContext;
    }

    private Context appContext;
    private List<Car> myList;

    public List<Car> getMyList() {
        return myList;
    }

    public void setMyList(List<Car> myList) {
        this.myList = myList;
    }

    public PurchasedListAdapter(Context appContext, ClientService service) {
        this.service = service;
        this.appContext = appContext;
        this.myList = service.getPurchasedList();
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return myList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") final View v = View.inflate(this.appContext, R.layout.view_car_purchased, null);

        TextView name = v.findViewById(R.id.textView8);
        TextView quantity = v.findViewById(R.id.textView9);
        TextView type = v.findViewById(R.id.textView10);

        Button returnButton = v.findViewById(R.id.button10);

        final Car current = myList.get(i);

        name.setText(current.getName());
        quantity.setText(current.getQuantity().toString());
        type.setText(current.getType());

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safeReturn(current);
            }
        });
        return v;
    }

    private void safeReturn(final Car current) {
        boolean network = MainActivity.networkConnectivity(MainActivity.clientService.getClientListAdapter().getAppContext());
        if(!network)
        {
            Snackbar.make(PurchasedCarsActivity.instance.findViewById(R.id.listviewpurchased), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            safeReturn(current);
                        }
                    }).show();
        }
        else {
            service.doReturn(PurchasedCarsActivity.instance, current);
        }
    }
}

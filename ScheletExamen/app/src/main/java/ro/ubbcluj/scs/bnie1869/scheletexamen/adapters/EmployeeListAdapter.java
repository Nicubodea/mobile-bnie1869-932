package ro.ubbcluj.scs.bnie1869.scheletexamen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.scheletexamen.EmployeeActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.MainActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.R;
import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;
import ro.ubbcluj.scs.bnie1869.scheletexamen.service.EmployeeService;

/**
 * Created by nbodea on 2/2/2018.
 */

public class EmployeeListAdapter extends BaseAdapter {
    private Context appContext;

    public Context getAppContext() {
        return appContext;
    }

    public List<Car> getMyList() {
        return myList;
    }

    public void setMyList(List<Car> myList) {
        this.myList = myList;
    }

    private List<Car> myList;
    private EmployeeService service;

    public EmployeeListAdapter(Context appContext, EmployeeService service) {
        this.myList = service.getList();
        this.appContext = appContext;
        this.service = service;
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
        @SuppressLint("ViewHolder") final View v = View.inflate(this.appContext, R.layout.view_car_employee, null);

        TextView name = v.findViewById(R.id.textView11);
        TextView quantity = v.findViewById(R.id.textView12);
        TextView type = v.findViewById(R.id.textView13);

        Button deletebutton = v.findViewById(R.id.button11);

        final Car current = myList.get(i);

        name.setText(current.getName());
        quantity.setText(current.getQuantity().toString());
        type.setText(current.getType());

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safeDelete(current);
            }
        });
        return v;
    }

    private void safeDelete(final Car current) {
        boolean network = MainActivity.networkConnectivity(MainActivity.clientService.getClientListAdapter().getAppContext());
        if(!network)
        {
            Snackbar.make(EmployeeActivity.instance.findViewById(R.id.listviewemployee), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            safeDelete(current);
                        }
                    }).show();
        }
        else {
            service.doDelete(EmployeeActivity.instance, current);
        }
    }
}

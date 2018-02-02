package ro.ubbcluj.scs.bnie1869.scheletexamen.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import ro.ubbcluj.scs.bnie1869.scheletexamen.EmployeeActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.adapters.EmployeeListAdapter;
import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;

/**
 * Created by nbodea on 2/2/2018.
 */

public class EmployeeService {
    private AsyncHttpClient client = new AsyncHttpClient();
    private List<Car> cars;
    private EmployeeListAdapter employeeListAdapter;
    private String SERVER_PATH = "http://192.168.0.157:4000";
    private String TAG = "EmployeeService";

    public EmployeeService(List<Car> cars) {
        this.cars = cars;
    }

    public EmployeeListAdapter getEmployeeListAdapter() {
        return employeeListAdapter;
    }

    public void setEmployeeListAdapter(EmployeeListAdapter employeeListAdapter) {
        this.employeeListAdapter = employeeListAdapter;
    }

    private ProgressDialog createNewProgress(Context context, String title, String message){
        ProgressDialog nDialog;
        nDialog = new ProgressDialog(context);
        nDialog.setMessage(message);
        nDialog.setTitle(title);
        nDialog.setIndeterminate(true);
        nDialog.setCancelable(false);
        nDialog.show();
        return nDialog;
    }

    public void doDelete(Context context, final Car car) {
        final ProgressDialog dialog = createNewProgress(context, "Loading", "Deleting car");
        RequestParams rp = new RequestParams();
        rp.add("id", car.getId().toString());
        Log.v(TAG, "Calling doDelete");
        client.post(SERVER_PATH+"/removeCar", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                for(int i = 0; i<cars.size(); i++) {
                    if(Objects.equals(cars.get(i).getId(), car.getId())) {
                        cars.remove(i);
                    }
                }
                employeeListAdapter.notifyDataSetChanged();
                dialog.dismiss();
                Log.v(TAG, "doDelete success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(employeeListAdapter.getAppContext(), "Cannot delete element", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                Log.v(TAG,"doDelete failure");
            }
        });
    }

    public List<Car> getList() {
        return cars;
    }

    public void doAdd(Context context, final Car car) {
        final ProgressDialog dialog = createNewProgress(context, "Loading", "Adding car");
        RequestParams rp = new RequestParams();
        rp.add("name", car.getName());
        rp.add("type", car.getType());

        Log.v(TAG, "Calling doAdd");
        client.post(SERVER_PATH+"/addCar", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                for(int i = 0; i<cars.size(); i++) {
                    if(cars.get(i).getName().compareTo(car.getName()) == 0 &&
                            cars.get(i).getType().compareTo(car.getType()) == 0) {
                        cars.get(i).setQuantity(cars.get(i).getQuantity()+1);
                    }
                }

                dialog.dismiss();
                Log.v(TAG, "doAdd success");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(employeeListAdapter.getAppContext(), "Cannot add car", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                Log.v(TAG, "doAdd failure");
            }
        });
    }

    public void getAll(Context context) {
        final ProgressDialog dialog = createNewProgress(context, "Loading", "Getting cars");
        Log.v(TAG,"Calling getAll");
        client.get(SERVER_PATH+"/all", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                List<Car> newList = new ArrayList<>();
                for(int i = 0; i<response.length(); i++) {
                    try {
                        JSONObject current = response.getJSONObject(i);
                        Integer id = current.getInt("id");
                        String name = current.getString("name");
                        Integer quantity = current.getInt("quantity");
                        String type = current.getString("type");
                        String status = current.getString("status");
                        Car car = new Car(id, name, quantity, type, status);
                        newList.add(car);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
                cars = newList;
                employeeListAdapter.setMyList(cars);
                employeeListAdapter.notifyDataSetChanged();
                Log.v(TAG, "Get all finished!");
            }
        });


    }

    public void doAddNotification(Car car) {
        Log.v(TAG, "New add notification!");
        car.setQuantity(1);
        cars.add(car);
        try {
            EmployeeActivity.instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    employeeListAdapter.notifyDataSetChanged();
                }
            });
        } catch(Exception e) {
            Log.v(TAG, "EmployeeActivity not yet started!");
        }

    }
}


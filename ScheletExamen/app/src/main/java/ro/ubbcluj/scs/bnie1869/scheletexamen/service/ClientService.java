package ro.ubbcluj.scs.bnie1869.scheletexamen.service;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import ro.ubbcluj.scs.bnie1869.scheletexamen.ClientActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.EmployeeActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.MainActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.PurchasedCarsActivity;
import ro.ubbcluj.scs.bnie1869.scheletexamen.adapters.ClientListAdapter;
import ro.ubbcluj.scs.bnie1869.scheletexamen.adapters.PurchasedListAdapter;
import ro.ubbcluj.scs.bnie1869.scheletexamen.db.CarDao;
import ro.ubbcluj.scs.bnie1869.scheletexamen.db.CarDb;
import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;

/**
 * Created by nbodea on 2/2/2018.
 */

public class ClientService {

    private AsyncHttpClient client = new AsyncHttpClient();
    private List<Car> cars;
    private List<Car> purchasedCars;
    private ClientListAdapter clientListAdapter;
    private PurchasedListAdapter purchasedListAdapter;
    String TAG = "ClientService";

    private String SERVER_PATH = "http://192.168.0.157:4000";

    public ClientListAdapter getClientListAdapter() {
        return clientListAdapter;
    }

    public void setClientListAdapter(ClientListAdapter clientListAdapter) {
        this.clientListAdapter = clientListAdapter;
    }

    public PurchasedListAdapter getPurchasedListAdapter() {
        return purchasedListAdapter;
    }

    public void setPurchasedListAdapter(PurchasedListAdapter purchasedListAdapter) {
        this.purchasedListAdapter = purchasedListAdapter;
    }

    public ClientService(List<Car> cars, List<Car> purchasedCars) {

        this.cars = cars;
        this.purchasedCars = purchasedCars;
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

    public void doBuy(Context context, final Car car, final String quantity) {
        final ProgressDialog dialog = createNewProgress(context, "Loading", "Making buy operation");
        RequestParams rp = new RequestParams();
        rp.add("id", car.getId().toString());
        rp.add("quantity", quantity);
        Log.v(TAG, "Calling doBuy");
        client.post(SERVER_PATH+"/buyCar", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                doBuyLocal(car, quantity);
                dialog.dismiss();
                ClientActivity.instance.startActivity(new Intent(ClientActivity.instance, PurchasedCarsActivity.class));
                Log.v(TAG, "doBuy success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                dialog.dismiss();
                Toast.makeText(clientListAdapter.getAppContext(), "Invalid data given", Toast.LENGTH_LONG).show();
                Log.v(TAG, "doBuy failure");
            }
        });

    }

    public void doBuyLocal(Car car, String quantity) {
        try {
            CarDb db = MainActivity.database;
            CarDao dao = db.getCarDao();
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String cDate = dateFormat.format(date);
            Integer q = Integer.parseInt(quantity);
            if(q > car.getQuantity()) {
                throw new Exception();
            }
            Car newCar = new Car(car.getId(), car.getName(), q, car.getType(), car.getStatus(), cDate);

            try {
                Log.v(TAG, "Inserting purchase into DB");
                dao.insert(newCar);
                purchasedCars.add(newCar);
                purchasedListAdapter.notifyDataSetChanged();
            } catch(Exception e) {
                Log.v(TAG, "Insert failed, trying update");
                for(int i = 0; i<purchasedCars.size(); i++) {
                    if(purchasedCars.get(i).getId() == car.getId()) {
                        newCar.setQuantity(purchasedCars.get(i).getQuantity() + q);
                        purchasedCars.get(i).setQuantity(newCar.getQuantity());
                    }
                }
                dao.update(newCar);
                purchasedListAdapter.notifyDataSetChanged();
            }

            for(int i = 0; i<cars.size(); i++) {
                if(Objects.equals(cars.get(i).getId(), car.getId())) {
                    cars.get(i).setQuantity(cars.get(i).getQuantity() - q);
                }
            }

            clientListAdapter.notifyDataSetChanged();


        } catch(Exception e) {
            Log.v(TAG, "Exception "+e.getMessage()+" occurred while doing doBuy");
            Toast.makeText(clientListAdapter.getAppContext(), "Invalid data given", Toast.LENGTH_LONG).show();
        }
    }

    public List<Car> getList() {
        return cars;
    }

    public List<Car> getPurchasedList() {
        return purchasedCars;
    }

    public void doReturn(Context context, final Car car) {
        final ProgressDialog dialog = createNewProgress(context, "Loading", "Returning car");
        Log.v(TAG, "Calling doReturn");

        try {
            String string = car.getBuyTime();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = format.parse(string);
            Date today = new Date();
            if(TimeUnit.MILLISECONDS.toDays(today.getTime() - date.getTime()) > 30) {
                Log.v(TAG, "doReturn with more than 30 days");
                Toast.makeText(clientListAdapter.getAppContext(), "Cannot return car after 30 days", Toast.LENGTH_LONG).show();
                return;
            }
            RequestParams rp = new RequestParams();
            rp.add("id", car.getId().toString());
            Log.v(TAG, "/returnCar called");
            client.post(SERVER_PATH+"/returnCar", rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    doReturnLocal(car);
                    dialog.dismiss();
                    Log.v(TAG, "/returnCar success");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(clientListAdapter.getAppContext(), "Cannot return car", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    Log.v(TAG, "/returnCar failure");
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void doReturnLocal(Car car) {
        CarDao dao = MainActivity.database.getCarDao();
        Log.v(TAG, "DB delete returned");
        dao.delete(car);
        for(int i = 0; i<cars.size(); i++) {
            if(Objects.equals(cars.get(i).getId(), car.getId())) {
                cars.get(i).setQuantity(cars.get(i).getQuantity() + car.getQuantity());
            }
        }

        getAllPurchased();
    }

    public void getAll(Context context) {
        final ProgressDialog dialog = createNewProgress(context, "Loading", "Fetching car list from server");
        Log.v(TAG, "calling /cars");
        client.get(SERVER_PATH+"/cars", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                List<Car> currentCars = new ArrayList<>();

                for(int i = 0; i<response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Integer id = object.getInt("id");
                        String name = object.getString("name");
                        Integer quantity = object.getInt("quantity");
                        String type = object.getString("type");
                        String status = object.getString("status");

                        Car car = new Car(id, name, quantity, type, status);
                        currentCars.add(car);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                cars = currentCars;
                clientListAdapter.setCarList(cars);
                clientListAdapter.notifyDataSetChanged();
                dialog.dismiss();
                Log.v(TAG, "/cars success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                dialog.dismiss();
                Log.v(TAG, "/cars failure");
            }
        });
    }

    public void getAllPurchased() {
        Log.v(TAG, "Get all purchased from DB");
        CarDao dao = MainActivity.database.getCarDao();
        purchasedCars = dao.getAll();
        purchasedListAdapter.setMyList(purchasedCars);
        purchasedListAdapter.notifyDataSetChanged();

    }

    public void clearPurchased() {
        Log.v(TAG, "Clear DB purchased");
        CarDao dao = MainActivity.database.getCarDao();
        dao.emptyStorage();
        purchasedCars = new ArrayList<>();
        purchasedListAdapter.setMyList(purchasedCars);
        purchasedListAdapter.notifyDataSetChanged();

    }

    public void doAddNotification(Car car) {
        Log.v(TAG, "New add notification!");
        cars.add(car);
        try {
            ClientActivity.instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clientListAdapter.notifyDataSetChanged();
                }
            });
        } catch(Exception e) {
            Log.v(TAG, "ClientActivity not yet started!");
        }
    }

}

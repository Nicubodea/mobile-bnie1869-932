package ro.ubbcluj.scs.bnie1869.rentbike.utils;

import android.widget.ListView;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.rentbike.db.RentBikePlaceDB;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.model.Token;

/**
 * Created by nbodea on 1/13/2018.
 */

public class Globals {
    static public LocalStorage localStorage;
    static public List<RentBikePlace> rentBikePlaceList;
    static public List<RentBikePlace> showRentBikePlaceList;
    static public RentBikePlaceDB databaseSingleton;
    static public ListView listView;
    static public Boolean isAppConnected;
    static public Token token;
    static public Boolean isListLoaded;
    static public Boolean isLoggedIn;


    static public String SERVER_PATH = "http://192.168.0.157";
    static public String WS_SERVER_PATH;

}

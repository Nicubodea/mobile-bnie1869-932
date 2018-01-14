package ro.ubbcluj.scs.bnie1869.rentbike.utils;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.rentbike.db.RentBikePlaceDB;
import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.model.Token;

/**
 * Created by nbodea on 11/29/2017.
 */

public class LocalStorage {
    private RentBikePlaceDB database;

    public List<RentBikePlace> getList() {
        return database.rentBikePlaceDAO().getRentBikePlaces();
    }

    public LocalStorage(RentBikePlaceDB database) {
        this.database = database;
    }

    public void add(RentBikePlace x) {
        database.rentBikePlaceDAO().addRentBikePlace(x);
    }

    public void delete(RentBikePlace x) {
        database.rentBikePlaceDAO().deleteRentBikePlace(x);
    }

    public void update(RentBikePlace x) {
        database.rentBikePlaceDAO().updateRentBikePlace(x);
    }

    public Token getToken() {
        List<Token> tokens = database.tokenDAO().getRentBikePlaces();
        if(tokens.size() == 0) {
            return null;
        }
        return tokens.get(0);
    }

    public void newToken(String token) {
        Token x = new Token();
        x.token = token;
        database.tokenDAO().addToken(x);
    }

    public void deleteToken() {
        Token x = getToken();
        if(x != null)
            database.tokenDAO().deleteToken(x);
    }
}

package ro.ubbcluj.scs.bnie1869.rentbike;

import java.util.List;

/**
 * Created by nbodea on 11/29/2017.
 */

public class Synchronizer {
    private RentBikePlaceDB database;

    public List<RentBikePlace> getList() {
        return database.rentBikePlaceDAO().getRentBikePlaces();
    }

    public Synchronizer(RentBikePlaceDB database) {
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
}

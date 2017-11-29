package ro.ubbcluj.scs.bnie1869.rentbike;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by nbodea on 11/29/2017.
 */

@Database(entities = {RentBikePlace.class}, version = 1)
public abstract class RentBikePlaceDB extends RoomDatabase {
    public abstract RentBikePlaceDAO rentBikePlaceDAO();
}

package ro.ubbcluj.scs.bnie1869.rentbike.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;
import ro.ubbcluj.scs.bnie1869.rentbike.model.Token;

/**
 * Created by nbodea on 11/29/2017.
 */

@Database(entities = {RentBikePlace.class, Token.class}, version = 3)
public abstract class RentBikePlaceDB extends RoomDatabase {
    public abstract RentBikePlaceDAO rentBikePlaceDAO();
    public abstract TokenDAO tokenDAO();
}

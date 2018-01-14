package ro.ubbcluj.scs.bnie1869.rentbike.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.rentbike.model.RentBikePlace;

/**
 * Created by nbodea on 11/29/2017.
 */

@Dao
public interface RentBikePlaceDAO {
    @Query("SELECT * FROM rentbikeplaces")
    List<RentBikePlace> getRentBikePlaces();

    @Insert
    void addRentBikePlace(RentBikePlace rentBikePlace);

    @Delete
    void deleteRentBikePlace(RentBikePlace rentBikePlace);

    @Update
    void updateRentBikePlace(RentBikePlace rentBikePlace);


}

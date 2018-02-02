package ro.ubbcluj.scs.bnie1869.scheletexamen.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;

/**
 * Created by nbodea on 2/2/2018.
 */
@Dao
public interface CarDao {
    @Query("SELECT * FROM car")
    List<Car> getAll();

    @Insert
    long insert(Car car);

    @Update
    void update(Car car);

    @Delete
    void delete(Car car);

    @Query("DELETE FROM car")
    void emptyStorage();

}

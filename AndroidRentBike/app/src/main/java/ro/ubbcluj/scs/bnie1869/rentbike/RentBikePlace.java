package ro.ubbcluj.scs.bnie1869.rentbike;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.UUID;

/**
 * Created by nbodea on 11/5/2017.
 */

@Entity(tableName = "rentbikeplaces")
public class RentBikePlace {
    @PrimaryKey
            @ColumnInfo(name="address")
    String address;
            @ColumnInfo(name="number_bikes")
    Integer numberOfBikes;
            @ColumnInfo(name="number_available")
    Integer numberOfAvailableBikes;

    @Ignore
    public RentBikePlace(String address, Integer numberOfBikes, Integer numberOfAvailableBikes) {
        this.address = address;
        this.numberOfBikes = numberOfBikes;
        this.numberOfAvailableBikes = numberOfAvailableBikes;
    }

    public RentBikePlace() {
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNumberOfBikes() {
        return numberOfBikes;
    }

    public void setNumberOfBikes(Integer numberOfBikes) {
        this.numberOfBikes = numberOfBikes;
    }

    @Override
    public String toString() {
        return address + ": " + this.numberOfAvailableBikes.toString() + "/" + this.numberOfBikes.toString();
    }

    public Integer getNumberOfAvailableBikes() {
        return numberOfAvailableBikes;
    }

    public void setNumberOfAvailableBikes(Integer numberOfAvailableBikes) {
        this.numberOfAvailableBikes = numberOfAvailableBikes;
    }
}

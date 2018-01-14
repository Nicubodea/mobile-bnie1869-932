package ro.ubbcluj.scs.bnie1869.rentbike.model;

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
    public String address;
            @ColumnInfo(name="number_bikes")
    public Integer numberOfBikes;
            @ColumnInfo(name="number_available")
    public Integer numberOfAvailableBikes;
            @ColumnInfo(name="state")
    public String state;

    @Ignore
    public RentBikePlace(String address, Integer numberOfBikes, Integer numberOfAvailableBikes, String state) {
        this.address = address;
        this.numberOfBikes = numberOfBikes;
        this.numberOfAvailableBikes = numberOfAvailableBikes;
        this.state = state;
    }

    public RentBikePlace() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

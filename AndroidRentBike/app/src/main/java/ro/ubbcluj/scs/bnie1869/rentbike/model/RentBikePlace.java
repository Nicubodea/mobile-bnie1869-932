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
    public String street;
            @ColumnInfo(name="number_bikes")
    public Integer total;
            @ColumnInfo(name="number_available")
    public Integer available;
            @ColumnInfo(name="state")
    public String state;
            @ColumnInfo(name="active")
    public String active;

    @Ignore
    public RentBikePlace(String address, Integer numberOfBikes, Integer numberOfAvailableBikes, String state) {
        this.street = address;
        this.total = numberOfBikes;
        this.available = numberOfAvailableBikes;
        this.state = state;
        this.active = "Inactive";
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

        return street;
    }

    public void setAddress(String address) {
        this.street = address;
    }

    public Integer getNumberOfBikes() {
        return total;
    }

    public void setNumberOfBikes(Integer numberOfBikes) {
        this.total = numberOfBikes;
    }

    @Override
    public String toString() {
        return street + ": " + this.available.toString() + "/" + this.total.toString();
    }

    public Integer getNumberOfAvailableBikes() {
        return available;
    }

    public void setNumberOfAvailableBikes(Integer numberOfAvailableBikes) {
        this.available = numberOfAvailableBikes;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}

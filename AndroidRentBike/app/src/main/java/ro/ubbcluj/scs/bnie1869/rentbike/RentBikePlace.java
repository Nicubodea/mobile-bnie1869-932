package ro.ubbcluj.scs.bnie1869.rentbike;

/**
 * Created by nbodea on 11/5/2017.
 */

public class RentBikePlace {
    String address;
    Integer numberOfBikes;
    Integer numberOfAvailableBikes;

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
        return address;
    }

    public Integer getNumberOfAvailableBikes() {
        return numberOfAvailableBikes;
    }

    public void setNumberOfAvailableBikes(Integer numberOfAvailableBikes) {
        this.numberOfAvailableBikes = numberOfAvailableBikes;
    }
}

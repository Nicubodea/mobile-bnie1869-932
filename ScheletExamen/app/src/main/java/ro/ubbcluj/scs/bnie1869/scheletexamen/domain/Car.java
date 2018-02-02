package ro.ubbcluj.scs.bnie1869.scheletexamen.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by nbodea on 2/2/2018.
 */
@Entity(tableName = "car")
public class Car {
    @PrimaryKey
    private Integer id;
    private String name;
    private Integer quantity;
    private String type;
    private String status;
    private String buyTime;

    public Car() {

    }

    public Car(Integer id, String name, Integer quantity, String type, String status, String buyTime) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        this.buyTime = buyTime;
    }

    public Car(Integer id, String name, Integer quantity, String type, String status) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }
}

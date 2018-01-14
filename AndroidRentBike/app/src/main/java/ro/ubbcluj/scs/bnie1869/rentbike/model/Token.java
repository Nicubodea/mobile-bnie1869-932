package ro.ubbcluj.scs.bnie1869.rentbike.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by nbodea on 1/13/2018.
 */

@Entity(tableName = "tokens")
public class Token {
    @PrimaryKey
    @ColumnInfo(name="token")
    public String token;

    @Ignore
    public Token(String token) {
        this.token = token;
    }

    public Token() {

    }
}

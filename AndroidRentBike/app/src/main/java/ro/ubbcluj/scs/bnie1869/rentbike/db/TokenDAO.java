package ro.ubbcluj.scs.bnie1869.rentbike.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ro.ubbcluj.scs.bnie1869.rentbike.model.Token;

/**
 * Created by nbodea on 11/29/2017.
 */

@Dao
public interface TokenDAO {
    @Query("SELECT * FROM tokens")
    List<Token> getRentBikePlaces();

    @Insert
    void addToken(Token rentBikePlace);

    @Delete
    void deleteToken(Token rentBikePlace);

}

package ro.ubbcluj.scs.bnie1869.scheletexamen.db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ro.ubbcluj.scs.bnie1869.scheletexamen.domain.Car;

/**
 * Created by nbodea on 2/2/2018.
 */
@Database(entities = {Car.class}, version = 1, exportSchema = false)
public abstract class CarDb extends RoomDatabase {

        public abstract CarDao getCarDao();

        public static CarDb getAppDatabase(Context context) {
            return Room.databaseBuilder(context.getApplicationContext(), CarDb.class, "cars-database")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build();
        }
}

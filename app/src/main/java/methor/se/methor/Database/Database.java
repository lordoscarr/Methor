package methor.se.methor.Database;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import methor.se.methor.Models.User;

@android.arch.persistence.room.Database(entities = {User.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract DatabaseDaos.UserDao userDao();

    private static volatile Database INSTANCE;

   public static Database getDatabase(Activity activity) {
        if(INSTANCE == null){
            synchronized (Database.class) {
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(activity, Database.class,
                            "user_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
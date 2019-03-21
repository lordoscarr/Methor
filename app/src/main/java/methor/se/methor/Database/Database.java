package methor.se.methor.Database;

import android.arch.persistence.room.RoomDatabase;

import methor.se.methor.Models.User;

    @android.arch.persistence.room.Database(entities = {User.class}, version = 1)
public abstract class Database extends RoomDatabase {
        public abstract DatabaseDaos.UserDao userDao();
}

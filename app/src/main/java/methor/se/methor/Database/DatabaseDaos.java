package methor.se.methor.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import methor.se.methor.Models.User;

public class DatabaseDaos {

    @Dao
    public interface UserDao {
        @Query("SELECT * FROM user")
        List<User> getAllUsers();

        @Insert
        void insertUser(User user);
    }
}

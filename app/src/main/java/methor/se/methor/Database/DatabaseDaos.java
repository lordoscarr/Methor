package methor.se.methor.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import methor.se.methor.Models.User;

public class DatabaseDaos {

    @Dao
    public interface UserDao {
        @Query("SELECT * FROM user")
        List<User> getAllUsers();

        @Query("SELECT highscore FROM user WHERE username = :username")
        int getScore(String username);

        @Insert
        void insertUser(User user);

        @Query("SELECT * FROM user WHERE username IS (:username)")
        User checkUsername(String username);


        @Query("UPDATE user SET highscore =:score + highscore WHERE username=:username")
        void updateUser(int score, String username);
    }
}

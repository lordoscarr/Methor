package methor.se.methor.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "username")
    private String username;
    @ColumnInfo (name = "highscore")
    private int highscore;

    public User(String username){
        this.username = username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setHighscore(int highscore){
        this.highscore = highscore;
    }

    public int getHighscore(){
        return highscore;
    }
}

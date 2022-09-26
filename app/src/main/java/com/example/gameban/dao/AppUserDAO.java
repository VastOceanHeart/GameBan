package com.example.gameban.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gameban.entity.AppUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface AppUserDAO {
    @Query("SELECT * FROM appuser ORDER BY email ASC")
    @NotNull
    LiveData<List<AppUser>> getAll();

    @Query("SELECT * FROM appuser WHERE email = :email LIMIT 1")
    AppUser findByEmail(String email);

    @Insert
    void insert(AppUser appUser);

    @Delete
    void delete(AppUser appUser);

    @Update
    void updateAppUser(AppUser appUser);

    @Query("DELETE FROM appuser")
    void deleteAll();
}

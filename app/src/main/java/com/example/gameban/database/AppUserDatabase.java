package com.example.gameban.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gameban.dao.AppUserDAO;
import com.example.gameban.entity.AppUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AppUser.class}, version = 3, exportSchema = false)
public abstract class AppUserDatabase extends RoomDatabase {

    //Declare how many threads would be handle for this database
    private static final int NUMBER_OF_THREADS = 4;
    //Following part are used to create database
    public static final ExecutorService appUserDatabaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static AppUserDatabase INSTANCE;

    public static synchronized AppUserDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppUserDatabase.class, "AppUserDatabase").
                    fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract AppUserDAO appUserDAO();
}

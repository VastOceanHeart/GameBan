package com.example.gameban.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.gameban.dao.AppUserDAO;
import com.example.gameban.database.AppUserDatabase;
import com.example.gameban.entity.AppUser;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class AppUserRepository {
    private AppUserDAO appUserDAO;

    private LiveData<List<AppUser>> appUsers;

    public AppUserRepository(Application application) {
        AppUserDatabase audb = AppUserDatabase.getInstance(application);
        appUserDAO = audb.appUserDAO();
        appUsers = appUserDAO.getAll();
    }

    /**
     * Get all appUser from database
     */
    public LiveData<List<AppUser>> getAllAppUsers() {
        return appUsers;
    }

    /**
     * Insert a new appUser
     *
     * @param appuser The new appUser want to insert
     */
    public void insert(final AppUser appuser) {
        AppUserDatabase.appUserDatabaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                appUserDAO.insert(appuser);
            }
        });
    }

    /**
     * Delete a existing appUser
     *
     * @param appuser The appUser want to delete
     */
    public void delete(final AppUser appuser) {
        AppUserDatabase.appUserDatabaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                appUserDAO.delete(appuser);
            }
        });
    }

    /**
     * Update a existing appUser
     *
     * @param appuser The new information about targeted appUser
     */
    public void updateAppUser(final AppUser appuser) {
        AppUserDatabase.appUserDatabaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                appUserDAO.updateAppUser(appuser);
            }
        });
    }

    /**
     * Delete all appUsers in the database
     */
    public void deleteAll() {
        AppUserDatabase.appUserDatabaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                appUserDAO.deleteAll();
            }
        });
    }

    /**
     * Find a appUser based on specific email
     *
     * @param appUserEmail The email for target appUser
     * @return The found appUser
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<AppUser> findByEmailFuture(final String appUserEmail) {
        return CompletableFuture.supplyAsync(new Supplier<AppUser>() {
            @Override
            public AppUser get() {
                return appUserDAO.findByEmail(appUserEmail);
            }
        }, AppUserDatabase.appUserDatabaseWriteExecutor);
    }
}

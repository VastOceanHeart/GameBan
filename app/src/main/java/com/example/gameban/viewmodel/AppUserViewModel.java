package com.example.gameban.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gameban.entity.AppUser;
import com.example.gameban.repository.AppUserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AppUserViewModel extends AndroidViewModel {
    private AppUserRepository appUserRepository;
    private LiveData<List<AppUser>> appUsers;

    public AppUserViewModel(Application application) {
        super(application);
        appUserRepository = new AppUserRepository(application);
        appUsers = appUserRepository.getAllAppUsers();
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
        appUserRepository.insert(appuser);
    }

    /**
     * Delete a existing appUser
     *
     * @param appuser The appUser want to delete
     */
    public void delete(final AppUser appuser) {
        appUserRepository.delete(appuser);
    }

    /**
     * Update a existing appUser
     *
     * @param appuser The new information about targeted appUser
     */
    public void updateAppUser(final AppUser appuser) {
        appUserRepository.updateAppUser(appuser);
    }

    /**
     * Delete all appUsers in the database
     */
    public void deleteAll() {
        appUserRepository.deleteAll();
    }

    /**
     * Find a appUser based on specific email
     *
     * @param appUserEmail The email for target appUser
     * @return The found appUser
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<AppUser> findByEmailFuture(final String appUserEmail) {
        return appUserRepository.findByEmailFuture(appUserEmail);
    }
}

package com.example.gameban.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.gameban.database.AppUserDatabase;
import com.example.gameban.entity.AppUser;
import com.example.gameban.viewmodel.AppUserViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseWorker extends Worker {

    public String currentUserEmail;
    private AppUserViewModel appUserViewModel;
    private DatabaseReference databaseReference;

    public FirebaseWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }


    @Override
    public Result doWork() {

        Context applicationContext = getApplicationContext();
        try {
            //Initialize the Firebase database
            databaseReference = FirebaseDatabase.getInstance().getReference();

            //Print messages with method name in Log
            Log.d(Thread.currentThread().getStackTrace()[2].getFileName(), Thread.currentThread().getStackTrace()[2].getMethodName() + "(): Receive current login user's email.");

            //Receive current login user's email
            currentUserEmail = getInputData().getString("userEmail");


            //Print messages with method name in Log
            Log.d(Thread.currentThread().getStackTrace()[2].getFileName(), Thread.currentThread().getStackTrace()[2].getMethodName() + "(): Find current user information from Room based on the user's email.");

            //Find current user information from Room based on the user's email
            AppUser currentUser = AppUserDatabase.getInstance(applicationContext).appUserDAO().findByEmail(currentUserEmail);

            //Generate the userId, which is the email remove "@" and "."
            String userId = currentUser.email.replace("@", "");
            userId = userId.replace(".", "");

            //Print messages with method name in Log
            Log.d(Thread.currentThread().getStackTrace()[2].getFileName(), Thread.currentThread().getStackTrace()[2].getMethodName() + "(): Update this user in Firebase.");

            //Update this user in Firebase
            databaseReference.child("users").child(userId).setValue(currentUser);

            //Print messages with method name in Log
            Log.d(Thread.currentThread().getStackTrace()[2].getFileName(), Thread.currentThread().getStackTrace()[2].getMethodName() + "(): Synchronous success.");

            // Indicate whether the work finished successfully with the Result
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }
}
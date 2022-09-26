package com.example.gameban.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.gameban.databinding.FragmentWorkManagerExhibitBinding;
import com.example.gameban.entity.AppUser;
import com.example.gameban.viewmodel.AppUserViewModel;
import com.example.gameban.worker.FirebaseWorker;

import java.util.concurrent.CompletableFuture;

public class WorkManagerExhibitFragment extends Fragment {

    //Room related
    private AppUserViewModel appUserViewModel;
    private FragmentWorkManagerExhibitBinding binding;
    private String userInformation;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentWorkManagerExhibitBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //Get current login user's email
        Bundle loginBundle = getActivity().getIntent().getExtras();
        String currentUserEmail = loginBundle.get("email").toString();

        //Get current login user
        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);
        CompletableFuture<AppUser> currentUser = appUserViewModel.findByEmailFuture(currentUserEmail);


        currentUser.thenAccept(appUser -> {
            String userName = appUser.name;
            String userAge = Integer.toString(appUser.age);
            String userAddress = appUser.address;
            String userGender = appUser.gender;
            String userHistories = appUser.histories;
            userInformation = currentUserEmail + "&" + userName + "&" + userAge + "&" + userAddress + "&" + userGender + "&" + userHistories;

            binding.currentAppUserEmail.setText("📪 User Email: " + currentUserEmail);
            binding.currentAppUserName.setText("📝 User Name: " + userName);
            binding.currentAppUserAge.setText("🗓 User Age: " + userAge);
            binding.currentAppUserAddress.setText("🏡 User Address: " + userAddress);
            binding.currentAppUserGender.setText("💭 User Gender: " + userGender);
            binding.currentAppUserHistories.setText("📋 User Search Histories: " + userHistories);

            //The button which is used to clear the search history
            binding.clearHistoriesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appUser.histories = "";
                    appUserViewModel.updateAppUser(appUser);
                    binding.currentAppUserHistories.setText("📋 User Search Histories: " + appUser.histories);
                    Toast.makeText(view.getContext().getApplicationContext(), "Search history clear successfully", Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.manuallySynchronizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Print messages with method name in Log
                    Log.d(Thread.currentThread().getStackTrace()[2].getFileName(), Thread.currentThread().getStackTrace()[2].getMethodName() + "(): Store current user's email information into a Data object.");

                    //Store current user's email information into a Data object.
                    Data data = new Data.Builder().putString("userEmail", currentUserEmail).build();

                    //Print messages with method name in Log
                    Log.d(Thread.currentThread().getStackTrace()[2].getFileName(), Thread.currentThread().getStackTrace()[2].getMethodName() + "(): Initial worker and pass current user email to Firebase synchronized worker.");

                    //Initial worker and pass current user email to Firebase synchronized worker.
                    WorkRequest wr = new OneTimeWorkRequest.Builder(FirebaseWorker.class).setInputData(data).build();

                    //Print messages with method name in Log
                    Log.d(Thread.currentThread().getStackTrace()[2].getFileName(), Thread.currentThread().getStackTrace()[2].getMethodName() + "(): Enqueue the work.");

                    //Enqueue the work
                    WorkManager.getInstance(getActivity().getApplication()).enqueue(wr);
                    Toast.makeText(view.getContext().getApplicationContext(), "Synchronize successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(view.getContext().getApplicationContext(), "Synchronize unsuccessfully", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
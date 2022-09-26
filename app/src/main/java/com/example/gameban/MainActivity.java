package com.example.gameban;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.gameban.databinding.ActivityMainBinding;
import com.example.gameban.entity.AppUser;
import com.example.gameban.viewmodel.AppUserViewModel;
import com.example.gameban.worker.FirebaseWorker;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * MainActivity for GameBan
 *
 * @author Wenxiao Wu
 * @version  22/05/2022
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private AppUserViewModel appUserViewModel;
    private String userInformation;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Register this activity in activityContainer
        activityContainer.getInstance().addActivity(this);

        //Receive the login user from login part via the bundle
        Bundle loginBundle = getIntent().getExtras();
        String currentUserEmail = loginBundle.get("email").toString();

        //Interactive with the room
        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AppUserViewModel.class);
        CompletableFuture<AppUser> appUserCompletableFuture = appUserViewModel.findByEmailFuture(currentUserEmail);

        //Find current login user and modify the navigation drawer
        appUserCompletableFuture.thenAccept(appUser -> {
            if (appUser != null) {
                /*
                Set the navigation drawer's header to current user name

                Gobeze, B. (2016). How to change text of a TextView in navigation drawer header?. Retrieved 6 May 2022, from https://stackoverflow.com/questions/34973456/how-to-change-text-of-a-textview-in-navigation-drawer-header
                 */
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                TextView currentUserName = (TextView) headerView.findViewById(R.id.currentAppUserName);
                currentUserName.setText(appUser.name);

                /*
                Usr worker manager to schedule update the current user information to Firebase
                */
                //First, get the data of current user
                String userName = appUser.name;
                String userAge = Integer.toString(appUser.age);
                String userAddress = appUser.address;
                String userGender = appUser.gender;
                String userHistories = appUser.histories;
                userInformation = currentUserEmail + "&" + userName + "&" + userAge + "&" + userAddress + "&" + userGender + "&" + userHistories;
            } else {
                Toast.makeText(this, "Does not find this user in database!", Toast.LENGTH_LONG).show();
            }
        });

        //Use the toolbar to instead actionbar
        setSupportActionBar(binding.appBar.toolbar);

        //Configure the navigation drawer
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home_fragment, R.id.nav_history_fragment, R.id.nav_report_fragment, R.id.nav_map_fragment, R.id.nav_work_manager_exhibit_fragment, R.id.nav_logout_fragment)
                .setOpenableLayout(binding.drawerLayout).build();

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navView, navController);

        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController, appBarConfiguration);


        //Use Data to pass current user information to worker
        Data data = new Data.Builder().putString("userEmail", currentUserEmail).build();

        //Initialize worker to schedule a synchronize worker
        PeriodicWorkRequest synchronizeToFirebase =
                new PeriodicWorkRequest.Builder(FirebaseWorker.class, 20, TimeUnit.MINUTES)
                        .setInputData(data)
                        .build();

        //Enqueue the schedule work
        WorkManager.getInstance(this).enqueue(synchronizeToFirebase);
    }

    /**
     * Handle the back button for a specific fragment
     * <p>
     * Shynline. (2018). How to handle back button when at the starting destination of the navigation component. Retrieved 11 May 2022, from https://stackoverflow.com/questions/50937116/how-to-handle-back-button-when-at-the-starting-destination-of-the-navigation-com
     */
    @Override
    public void onBackPressed() {
        if (Navigation.findNavController(this, R.id.nav_host_fragment)
                .getCurrentDestination().getId() == R.id.nav_home_fragment) {
            //If the user is already in the home page (home fragment), the system will reject his request to continue back
            Toast.makeText(this, "Please select \"Switch Account\" if you want to quit", Toast.LENGTH_LONG).show();
        } else
            super.onBackPressed();
    }
}

package com.example.gameban.loginSignup;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gameban.R;
import com.example.gameban.activityContainer;
import com.example.gameban.databinding.LoginPageBinding;

public class LoginPage extends AppCompatActivity {
    private LoginPageBinding binding;


    public LoginPage() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activityContainer.getInstance().addActivity(this);
        replaceFragment(new LoginFragment());

    }

    public void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, nextFragment);
        fragmentTransaction.commit();
    }
}

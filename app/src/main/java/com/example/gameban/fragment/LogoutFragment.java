package com.example.gameban.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.gameban.activityContainer;
import com.example.gameban.databinding.FragmentLogoutBinding;
import com.example.gameban.loginSignup.LoginPage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LogoutFragment extends Fragment {
    private FragmentLogoutBinding binding;
    private DatabaseReference databaseReference;

    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activityContainer.getInstance().finishAllActivity();
                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);
            }
        });

        binding.noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }
}
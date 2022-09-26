package com.example.gameban.loginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.gameban.activityContainer;
import com.example.gameban.databinding.FragmentExitAppBinding;


public class ExitAppFragment extends Fragment {
    private FragmentExitAppBinding binding;

    public ExitAppFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExitAppBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activityContainer.getInstance().finishAllActivity();

            }
        });
        binding.noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);
            }
        });
        return view;

    }


}


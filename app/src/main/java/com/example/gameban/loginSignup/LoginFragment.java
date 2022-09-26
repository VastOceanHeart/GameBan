package com.example.gameban.loginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.gameban.MainActivity;
import com.example.gameban.R;
import com.example.gameban.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private FirebaseAuth auth;
    private boolean loginFlag;
    private boolean passwordFlag;

    public LoginFragment() {
        loginFlag = false;
        passwordFlag = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.passwordCheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24));
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailEnter = binding.emailEditText.getText().toString();
                String passwordEnter = binding.passwordEditText.getText().toString();
                if (emailEnter.isEmpty() || passwordEnter.isEmpty()) {
                    binding.errorText.setText("E-mail or Password can't be empty");
                } else {
                    binding.errorText.setText("");
                    userVerify(emailEnter, passwordEnter);
                    binding.loginProgressBar.setVisibility(View.VISIBLE);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            if (loginFlag == false)
                                binding.errorText.setText("Incorrect E-mail or Password");
                            runUiThread();
                        }
                    };
                    timer.schedule(task, 3000);
                }
            }
        });
        binding.clearEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.emailEditText.setText("");
            }
        });
        binding.clearPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.passwordEditText.setText("");
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginPage activity = (LoginPage) getActivity();
                activity.replaceFragment(new ExitAppFragment());
            }
        });
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginPage activity = (LoginPage) getActivity();
                activity.replaceFragment(new SignUpFragment());
            }
        });

        binding.passwordCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordFlag == true) {
                    binding.passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordFlag = false;
                    binding.passwordEditText.setSelection(binding.passwordEditText.getText().length());
                    binding.passwordCheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24));
                } else {
                    binding.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordFlag = true;
                    binding.passwordEditText.setSelection(binding.passwordEditText.getText().length());
                    binding.passwordCheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24));
                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.emailEditText.setText("");
        binding.passwordEditText.setText("");
        binding.loginProgressBar.setVisibility(View.GONE);
        loginFlag = false;
    }

    public void userVerify(String emailEnter, String passwordEnter) {
        auth.signInWithEmailAndPassword(emailEnter, passwordEnter).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                loginFlag = true;
                binding.errorText.setText("");
                sendMessage("Login Successful");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", emailEnter);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //Send message
    public void sendMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void runUiThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.loginProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
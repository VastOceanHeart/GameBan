package com.example.gameban.loginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gameban.databinding.FragmentSignUpBinding;
import com.example.gameban.entity.AppUser;
import com.example.gameban.viewmodel.AppUserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment {

    private static final String TAG = "debuging";
    private FragmentSignUpBinding binding;
    private FirebaseAuth auth;
    private boolean emailFlag;
    private boolean passwordFlag;
    private boolean checkPasswordFlag;
    private boolean nicknameFlag;
    private boolean ageFlag;
    private boolean addressFlag;


    private DatabaseReference databaseReference;

    public SignUpFragment() {

        emailFlag = false;
        passwordFlag = false;
        checkPasswordFlag = false;
        passwordFlag = false;
        nicknameFlag = false;
        ageFlag = false;
        addressFlag = false;

    }

    //Check input e-mail

    public static boolean isEmail(String email) {
        String str = "message";
        if (null == email || "".equals(email)) return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        //Pattern p =  Pattern.compile("^[a-zA-Z][\\\\w\\\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\\\w\\\\.-]*[a-zA-Z0-9]\\\\.[a-zA-Z][a-zA-Z\\\\.]*[a-zA-Z]$\"");//Complex match
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //Check input password length
    public static boolean isPasswordMatchLength(String password) {
        if (password.length() >= 6 && password.length() <= 20)
            return true;
        return false;
    }

    //Check input Nickname is empty
    public static boolean isNicknameEmpty(String nickname) {

        if (nickname.isEmpty())
            return true;
        return false;
    }

    //Check age is not empty and not over 200
    public static boolean isAgeEmpty(String age) {
        if (age.isEmpty())
            return true;
        return false;
    }

    //Check age is less than 200
    public static boolean isAgeLess(int age) {
        if (age <= 200 && age >= 1)
            return true;
        return false;
    }

    //Check input address is empty
    public static boolean isAddressEmpty(String address) {
        if (address.trim().isEmpty() || address.trim().length() < 5)
            return true;
        return false;
    }

    //Check RadioGroup is checked
    public static boolean isRgChecked(RadioGroup radioGroup) {
        for (int i = 0; i < 3; i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                return true;
            }
        }
        return false;
    }

    //Check password again
    public static boolean isPasswordMatch(String password1, String password2) {
        if (password1.equals(password2))
            return true;
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Thread.currentThread().getStackTrace();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRgChecked(binding.rg) && emailFlag && passwordFlag && checkPasswordFlag && nicknameFlag && ageFlag && addressFlag) {
                    RadioButton rgb = binding.rg.findViewById(binding.rg.getCheckedRadioButtonId());
                    String gender = rgb.getText().toString();
                    String password = binding.passwordEditText.getText().toString();
                    String nickname = binding.nickNameEditText.getText().toString();
                    String email = binding.emailEditText.getText().toString();
                    int age = Integer.parseInt(binding.ageEditText.getText().toString());
                    String address = binding.addressEditText.getText().toString();
                    String userID = userIdGenerator(email);
                    AppUser newAppUser = new AppUser(email, nickname, age, address, gender, "");
                    registerUser(email, password, userID, newAppUser);
                } else
                    sendMessage("Please fill out each attributes");
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginPage activity = (LoginPage) getActivity();
                activity.replaceFragment(new LoginFragment());
            }
        });

        //Listen email bar to ban input blank
        binding.emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String[] str = charSequence.toString().split(" ");
                    String str1 = "";
                    for (int k = 0; k < str.length; k++) {
                        str1 += str[k];
                    }
                    binding.emailEditText.setText(str1);
                    binding.emailEditText.setSelection(i1);
                    Toast.makeText(getActivity(), "You can't enter blank in your E-mail address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = binding.emailEditText.getText().toString();
                if (isEmail(email)) {
                    binding.errorEmailText.setText("");
                    emailFlag = true;
                } else {
                    binding.errorEmailText.setText("Please enter valid email address");
                    emailFlag = false;
                }
            }

        });

        //Listen password input change
        binding.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String[] str = charSequence.toString().split(" ");
                    String str1 = "";
                    for (int k = 0; k < str.length; k++) {
                        str1 += str[k];
                    }
                    binding.passwordEditText.setText(str1);
                    binding.passwordEditText.setSelection(i);
                    Toast.makeText(getActivity(), "You can't enter blank in your password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = binding.passwordEditText.getText().toString();
                if (isPasswordMatchLength(password)) {
                    binding.errorPasswordText.setText("");
                    passwordFlag = true;
                } else {
                    binding.errorPasswordText.setText("Your password must over 6 characters long and less than 20");
                    passwordFlag = false;
                }
            }
        });

        //Listen checkPassword input change
        binding.checkPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String[] str = charSequence.toString().split(" ");
                    String str1 = "";
                    for (int k = 0; k < str.length; k++) {
                        str1 += str[k];
                    }
                    binding.checkPasswordEditText.setText(str1);
                    binding.checkPasswordEditText.setSelection(i);
                    Toast.makeText(getActivity(), "You can't enter blank in your password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password1 = binding.passwordEditText.getText().toString();
                String password2 = binding.checkPasswordEditText.getText().toString();
                if (isPasswordMatch(password1, password2)) {
                    binding.errorCheckPasswordText.setText("");
                    checkPasswordFlag = true;
                } else {
                    binding.errorCheckPasswordText.setText("Two password inputted were not match ");
                    checkPasswordFlag = false;
                }
            }
        });

        //Listen nickname input change
        binding.nickNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String nickname = binding.nickNameEditText.getText().toString().trim();
                if (isNicknameEmpty(nickname)) {
                    binding.errorNicknameText.setText("Nickname can't be empty");

                    nicknameFlag = false;

                } else {
                    binding.errorNicknameText.setText("");
                    nicknameFlag = true;
                }
            }


        });

        binding.ageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String age = binding.ageEditText.getText().toString();
                if (isAgeEmpty(age))
                    binding.errorAgeText.setText("Age can't be empty");
                else {
                    Integer ageInt = Integer.parseInt(age);
                    if (isAgeLess(ageInt)) {
                        binding.errorAgeText.setText("");
                        ageFlag = true;
                    } else {
                        binding.errorAgeText.setText("Age must less than 200 and more than 0");
                        binding.ageEditText.setText("");
                        ageFlag = false;
                    }
                }
            }
        });
        binding.addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    sendMessage("Invalid address will make map service unavailable");
            }
        });
        binding.addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String address = binding.addressEditText.getText().toString();
                if (isAddressEmpty(address)) {
                    binding.errorAddressText.setText("Address should over 5 characters");
                    addressFlag = false;
                } else {
                    binding.errorAddressText.setText("");
                    addressFlag = true;
                }
            }
        });

        //Listen clear buttons
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
        binding.clearCheckPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.checkPasswordEditText.setText("");
            }
        });
        binding.clearNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.nickNameEditText.setText("");
            }
        });
        binding.clearAgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ageEditText.setText("");
            }
        });
        binding.clearAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addressEditText.setText("");
            }
        });
        return view;
    }

    //send messages
    public void sendMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    //Register User on firebase
    private void registerUser(String email, String password, String userID, AppUser newAppUser) {
        // To create username and password
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendMessage("Registration Successful");
                    writeNewUser(userID, newAppUser);
                    AppUserViewModel appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);
                    appUserViewModel.insert(newAppUser);


                    startActivity(new Intent(getActivity(), LoginPage.class));
                } else
                    Toast.makeText(getActivity(), "Registration Unsuccessful, Email is occupied ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void writeNewUser(String userId, AppUser appUser) {
        databaseReference.child("users").child(userId).setValue(appUser);
    }

    public String userIdGenerator(String email) {
        String userId = email.replace("@", "");
        userId = userId.replace(".", "");
        return userId;
    }
}
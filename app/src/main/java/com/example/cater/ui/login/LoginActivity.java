package com.example.cater.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.cater.MainActivity;
import com.example.cater.R;
import com.example.cater.databinding.ActivityLoginBinding;
import com.example.cater.login.Login;
import com.example.cater.login.LoginRepository;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_REPLY =
            "com.example.android.LoginActivity.extra.REPLY";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private String mPhonePrefix;
    private String mPhone;
    private String mPassword;
    private Location mLastLocation;

    private ProfileViewModel profileViewModel;
    private LoginRepository loginRepository;
    private LoginViewModel loginViewModel;

    private EditText phoneEditText;
    private EditText passwordEditText;
    private Spinner phoneSpinner;
    private Button loginButton;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.cater.databinding.ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        loginRepository = new LoginRepository(getApplication());
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        phoneEditText = binding.phone;
        passwordEditText = binding.password;
        phoneSpinner = binding.phoneSpinner;
        loginButton = binding.login;
        registerButton = binding.register;

        if (phoneSpinner != null) {
            phoneSpinner.setOnItemSelectedListener(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.phone_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            phoneSpinner.setAdapter(adapter);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTask();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhone = mPhonePrefix + phoneEditText.getText().toString();
                mPassword = passwordEditText.getText().toString();
                new RegisterAsyncTask(profileViewModel, loginRepository).execute(
                        mPhone, mPassword);
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    phoneEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(phoneEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
    }

    private void loginTask() {
        mPhone = mPhonePrefix + phoneEditText.getText().toString();
        mPassword = passwordEditText.getText().toString();
        loginRepository.getLogin(mPhone, mPassword).observe(this, new Observer<Login>() {
            @Override
            public void onChanged(Login login) {
                if (login != null) {
                    replyMe(login.getUid());
                } else {
                    View view = LoginActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    passwordEditText.getText().clear();
                    Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterAsyncTask extends AsyncTask<String, Void, Integer> {
        private final ProfileViewModel profileViewModel;
        private final LoginRepository loginRepository;

        RegisterAsyncTask(ProfileViewModel model, LoginRepository login) {
            profileViewModel = model;
            loginRepository = login;
        }

        @Override
        protected Integer doInBackground(String... params) {
            int new_id = profileViewModel.getTotalCount();
            Login login = new Login(new_id, params[0], params[1]);
            loginRepository.insert(login);

            Profile profile = new Profile
                    .Builder(new_id, params[0])
                    .name("+" + params[0])
                    .description("Please put your description here")
                    .age(18)
                    .photo("default_1")
                    .active(false)
                    .builder();
            if (mLastLocation != null) {
                profile.setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                profile.setActive(true);
            }
            profileViewModel.insert(profile);

            return new_id;
        }

        protected void onPostExecute(Integer result) {
            replyMe(result);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String label = adapterView.getItemAtPosition(i).toString();
        mPhonePrefix = label.substring(1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mPhonePrefix = "852";
    }

    private void enableLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mLastLocation = location;
                        }
                    }
            );
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            } else {
                Toast.makeText(this, "Location Permission Denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void replyMe(int result) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, (int) result);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
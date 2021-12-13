package com.example.cater.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.cater.R;
import com.example.cater.databinding.ActivityLoginBinding;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY =
            "com.example.android.LoginActivity.extra.REPLY";
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new loginAsyncTask(profileViewModel, passwordEditText).execute(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new registerAsyncTask(profileViewModel).execute(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private  class loginAsyncTask extends AsyncTask<String, Void, Integer> {

        private ProfileViewModel mAsyncTaskViewModel;
        private WeakReference<EditText> passwordText;
        loginAsyncTask(ProfileViewModel model, EditText text) {
            mAsyncTaskViewModel = model;
            passwordText = new WeakReference<>(text);
        }

        @Override
        protected Integer doInBackground(String... params) {

            Profile[] profiles = mAsyncTaskViewModel.getAnyProfile();
            if (profiles == null) {
                Log.d(LoginActivity.class.toString(), "Profiles are null");
            } else {
                for (int i = 0; i < profiles.length; i++) {
                    Log.d(LoginActivity.class.toString(), profiles[i].getuPhone());
                }
            }
            return mAsyncTaskViewModel.login(params[0], params[1]);
        }

        protected void onPostExecute(Integer result) {
            Log.d(LoginActivity.class.toString(), result.toString().concat("this is current uid"));

            if(result == 0) {
                View view = LoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showLoginFailed(R.string.login_failed);
                passwordText.get().getText().clear();
            } else {
               replyMe(result);
            }
        }
    }

    private class registerAsyncTask extends AsyncTask<String, Void, Integer> {
        private ProfileViewModel mAsyncTaskViewModel;

        registerAsyncTask(ProfileViewModel model) {
            mAsyncTaskViewModel = model;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int new_id = mAsyncTaskViewModel.getIdCount();
            Log.d(LoginActivity.class.toString(), String.valueOf(new_id));
            Profile profile = new Profile
                    .Builder(new_id, strings[0], strings[1])
                    .name("")
                    .description("Please put your description here")
                    .photo("default_1")
                    .age(0)
                    .builder();

            mAsyncTaskViewModel.insert(profile);

            return new_id;
        }

        protected void onPostExecute(Integer result) {
            replyMe(result);
        }
    }

    private void replyMe(int result) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, (int)result);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
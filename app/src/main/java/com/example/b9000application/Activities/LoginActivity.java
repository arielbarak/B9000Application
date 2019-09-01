package com.example.b9000application.Activities;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.b9000application.Models.UserRepository;
import com.example.b9000application.Models.ViewModel.UserViewModel;
import com.example.b9000application.R;


@SuppressWarnings("ConstantConditions")
public class LoginActivity extends AppCompatActivity {
    private UserViewModel mUserViewModel;
    private EditText userMail, userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;

    private Intent mainActivity;
    private ImageView loginPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userMail = findViewById(R.id.logingMail);
        userPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.loginProgressBar);
        loginPhoto = findViewById(R.id.login_Photo);

        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        mainActivity = new Intent(this, MainActivity.class);
        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if ( mail.isEmpty() || password.isEmpty())
                {
                    showMessage("Please Verify all fields");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    signIn(mail,password);
                }
            }
        });
    }
    private void enableInputs ( boolean flag)
    {

        userMail.setEnabled(flag);
        userPassword.setEnabled(flag);
        loginPhoto.setEnabled(flag);
    }
    private void signIn(String mail, String password) {
        enableInputs(false);
        mUserViewModel.signIn(mail, password, new UserRepository.SignInListener() {
            @Override
            public void onSuccess() {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                updateUI();
            }

            @Override
            public void onFailer(Exception e) {
                showMessage(e.getMessage());
                enableInputs(true);
                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onOffiline() {

                showMessage("No Internet Connection!");
                enableInputs(true);
                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateUI() {
       startActivity(mainActivity);
       finish();
    }



    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}

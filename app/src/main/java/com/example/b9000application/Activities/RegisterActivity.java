package com.example.b9000application.Activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.b9000application.Models.Entities.UserAuth;
import com.example.b9000application.Models.UserRepository;
import com.example.b9000application.Models.ViewModel.UserViewModel;
import com.example.b9000application.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView ImgUserPhoto = null;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickerImgUri = null;
    private UserViewModel mUserViewModel;
    private EditText userEmail, userName, userPassword, userPassword2;
    private ProgressBar loadingProgress;
    private Button btn;
    private TextView signin_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        //initals
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        signin_btn = findViewById(R.id.reg_signin_btn);
        userPassword2 = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);
        loadingProgress = findViewById(R.id.regProgressBar);
        btn = findViewById(R.id.regBtn);
        loadingProgress.setVisibility(View.INVISIBLE);
        signin_btn.setPaintFlags(signin_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(signinActivity);
                finish();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAuth userAuth = null;
                boolean flag = true;
                enableInputs(false);
                btn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();
                try {
                    userAuth = new UserAuth(name,email,password,password2,pickerImgUri);
                    flag = true;
                } catch (Exception e) {
                    showMessage(e.getMessage());
                    btn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                    enableInputs(true);
                    flag = false;


                }
                if (flag){
                    CreateUserAccount(userAuth);
                }

            }
        });
        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkandrequestforpermission();

                } else {
                    openGallery();

                }
            }
        });
    }
        private void enableInputs ( boolean flag)
        {

            userEmail.setEnabled(flag);
            userName.setEnabled(flag);
            userPassword.setEnabled(flag);
            userPassword2.setEnabled(flag);
            signin_btn.setEnabled(flag);
        }
        private void CreateUserAccount( final UserAuth userAuth){

            mUserViewModel.signUp(userAuth.getEmail(), userAuth.getPassword(), new UserRepository.SignUpListener() {
                @Override
                public void onSuccess() {
                    mUserViewModel.updateUserInfo(userAuth.getName(), userAuth.getUserImage(), new UserRepository.UpdateUserInfoListener() {
                        @Override
                        public void onSuccess(String uri) {
                            showMessage("Account Created");
                            enableInputs(true);
                            showMessage("Register Complete");
                            updateUI();
                        }

                        @Override
                        public void onFailer(Exception e) {
                            showMessage("Account Creation Failed " + e);
                        }

                        @Override
                        public void onOffiline() {
                            showMessage("No Internet Connection!");
                            btn.setVisibility(View.VISIBLE);
                            enableInputs(true);
                        }
                    });


                }

                @Override
                public void onFailer(Exception e) {
                    showMessage("Account Creation Failed " + e);
                    btn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                    enableInputs(true);
                }

                @Override
                public void onOffiline() {
                    showMessage("No Internet Connection!");
                    btn.setVisibility(View.VISIBLE);
                    enableInputs(true);
                }

            });
        }


        private void updateUI () {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        private void showMessage (String message){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
                pickerImgUri = data.getData();
                ImgUserPhoto.setImageURI(pickerImgUri);
            }
        }

        private void openGallery () {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, REQUESTCODE);
        }

        private void checkandrequestforpermission () {
            if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);

            } else {
                openGallery();
            }
        }

}
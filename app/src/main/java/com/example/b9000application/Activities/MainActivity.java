package com.example.b9000application.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;


import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.ViewModel.PostInsertViewModel;
import com.example.b9000application.Models.PostRepository;
import com.example.b9000application.Models.ViewModel.UserViewModel;

import com.example.b9000application.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    private Intent loginActivity;

    private ImageView popupPostImage;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickerImgUri = null;
    CircleImageView  popupUserImage;
    private Button popupAddBtn;
    private ProgressBar popupClickProgress;
    TextView popupContent;
    Spinner popupCategory;
    Dialog popAddPost;
    private PostInsertViewModel mPostInsertViewModel;
    private UserViewModel mUserViewModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //init
        loginActivity = new Intent(this, LoginActivity.class);

        mPostInsertViewModel = ViewModelProviders.of(this).get(PostInsertViewModel.class);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        // ini popup
        iniPopup();
        setupPopupImageClick();
        FloatingActionButton fab =findViewById(R.id.addPostBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();

            }
        });


        navController = Navigation.findNavController(this,R.id.main_navhost_frag);
        BottomNavigationView bottomNavigationView =
                findViewById(R.id.bottomNavigationView);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        final androidx.appcompat.widget.Toolbar mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        //if i want the title will be the same
        NavigationUI.setupActionBarWithNavController(this,navController);
        mToolbar.setTitle("B9000");
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
              switch(destination.getId()){
                  case(R.id.profileFragment):
                  case(R.id.writersFragment):
                      assert getSupportActionBar()!=null;
                      getSupportActionBar().setDisplayHomeAsUpEnabled(false);

              }

            }
        });


    }

    public void hideKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view==null)
        {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private void setupPopupImageClick() {

    popupPostImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >=22){
                checkandrequestforpermission();

            }
            else{
                openGallery();

            }
        }
    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return navController.navigateUp();
        }
        else{
            return NavigationUI.onNavDestinationSelected(item,navController);
        }

    }

    private void enable_input(boolean b)
    {
        popupContent.setEnabled(b);
        popupPostImage.setEnabled(b);

    }




    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView((R.layout.add_post));
        popAddPost.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //ini popup widgiets
        popupUserImage = popAddPost.findViewById(R.id.add_post_user_image);
        popupPostImage = popAddPost.findViewById(R.id.add_post_image_post);
        popupContent = popAddPost.findViewById(R.id.add_post_content);
        popupCategory = popAddPost.findViewById(R.id.add_post_category);
        popupAddBtn = popAddPost.findViewById(R.id.add_post_send);
        popupClickProgress = popAddPost.findViewById(R.id.add_post_progressBar);
        popupAddBtn.setVisibility(View.VISIBLE);
        popupClickProgress.setVisibility(View.INVISIBLE);
        //load Current user Image
        Log.d("URI", "iniPopup: "+ mUserViewModel.getUserImageUrl());
        Glide.with(this).load(mUserViewModel.getUserImageUrl()).into(popupUserImage).waitForLayout();
        pickerImgUri = null;

        //Add ost Listner
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);
                enable_input(false);

                if (!popupContent.getText().toString().isEmpty() && pickerImgUri !=null)
                {
                    mPostInsertViewModel.saveBlogImage(pickerImgUri, new PostRepository.SaveImageListener() {
                        @Override
                        public void onComplete(String imageDownloadLink) {
                            Post post = new Post(popupCategory.getSelectedItem().toString(), popupContent.getText().toString(),imageDownloadLink,mUserViewModel.getUid(),0,mUserViewModel.getUserImageUrl().toString(),mUserViewModel.getDisplayName());
                            addPost(post);
                            enable_input(true);
                        }

                        @Override
                        public void onOffline() {
                            showMessage("Device not Connected can't upload the post");
                            popupAddBtn.setVisibility(View.VISIBLE);
                            popupClickProgress.setVisibility(View.INVISIBLE);
                            enable_input(true);
                        }

                        @Override
                        public void onError(Exception e) {
                            showMessage(e.getMessage());
                            popupAddBtn.setVisibility(View.VISIBLE);
                            popupClickProgress.setVisibility(View.INVISIBLE);
                            enable_input(true);
                        }
                    });
                }
                else
                {
                    showMessage("Please Verify all input fields and choose post Image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                    enable_input(true);
                }

            }
        });

    }

    private void addPost(Post post) {
        mPostInsertViewModel.insert(post, new PostRepository.InsertPostListener() {
            @Override
            public void onComplete(boolean success) {
                if (success){
                    showMessage("Post Added successfully");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                    popAddPost.hide();
                    iniPopup();
                    setupPopupImageClick();
                    enable_input(true);

                }
            }

            @Override
            public void onError(Exception e) {
                showMessage(e.getMessage());
                popupAddBtn.setVisibility(View.VISIBLE);
                popupClickProgress.setVisibility(View.INVISIBLE);
                enable_input(true);
            }

            @Override
            public void onOffline() {
                showMessage("Device not Connected can't upload the post");
                popupAddBtn.setVisibility(View.VISIBLE);
                popupClickProgress.setVisibility(View.INVISIBLE);
                enable_input(true);
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data!= null)
        {

            pickerImgUri = data.getData();
            popupPostImage.setPadding(0,0,0,0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupPostImage.setImageTintList(ColorStateList.valueOf(android.R.color.transparent));
            }
            popupPostImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            popupPostImage.setImageURI(pickerImgUri);

        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }

    private void checkandrequestforpermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);

        }
        else{
            openGallery();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mUserViewModel.isSigned())
            updateUI();


    }

    private void updateUI() {
        startActivity(loginActivity);
        finish();
    }

}

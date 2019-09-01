package com.example.b9000application.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.b9000application.Activities.MainActivity;
import com.example.b9000application.Activities.RegisterActivity;
import com.example.b9000application.Adapters.MyApplication;
import com.example.b9000application.Models.Entities.User;
import com.example.b9000application.Models.Entities.UserAuth;
import com.example.b9000application.Models.UserRepository;
import com.example.b9000application.Models.ViewModel.UserUpdateViewModel;
import com.example.b9000application.Models.ViewModel.UserViewModel;
import com.example.b9000application.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserFragment extends Fragment {

    Button updateButton;
    TextView nameTextView;
    CircleImageView imageView;
    ProgressBar progressBar ,loadProgressBar;
    UserUpdateViewModel userUpdateViewModel;
    ConstraintLayout layout;
    User user;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickerImgUri;
    public EditUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v  = inflater.inflate(R.layout.fragment_edit_user, container, false);
        userUpdateViewModel = ViewModelProviders.of(this).get(UserUpdateViewModel.class);
        updateButton = v.findViewById(R.id.user_edit_send_button);
        nameTextView = v.findViewById(R.id.user_edit_name_text_view);
        imageView = v.findViewById(R.id.image_user_edit);
        progressBar = v.findViewById(R.id.user_edit_progressBar);
        layout = v.findViewById(R.id.layout_user_edit);
        loadProgressBar = v.findViewById(R.id.progressBar_edit_user_load);
        loadProgressBar.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.VISIBLE);
        assert getArguments()!=null;
        user = EditUserFragmentArgs.fromBundle(getArguments()).getUser();
        populate();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = null;
                boolean flag = true;
                enableInputs(false);
                final String name = nameTextView.getText().toString();
                try {
                    if(pickerImgUri==null)
                        throw new Exception("Photo not selected");
                    u = new User(user.getUid(),user.getEmail(),name,pickerImgUri.toString());
                    flag = true;
                } catch (Exception e) {
                    showMessage(e.getMessage());
                    updateButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    enableInputs(true);
                    flag = false;

                }
                if (flag){
                    UpdateUser(u);
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkandrequestforpermission();

                } else {
                    openGallery();

                }
            }
        });
        return v;
    }

    private void UpdateUser(final User user) {

                userUpdateViewModel.updateUserInfo(user.getName(), pickerImgUri, new UserRepository.UpdateUserInfoListener() {
                    @Override
                    public void onSuccess(String uri) {
                        showMessage("User Updated");
                        enableInputs(true);
                        Navigation.findNavController(getView()).navigateUp();
                    }

                    @Override
                    public void onFailer(Exception e) {
                        showMessage("Error! "+e.getMessage());
                        enableInputs(true);
                    }

                    @Override
                    public void onOffiline() {
                        showMessage("Not Connection!");
                        enableInputs(true);
                    }
                });



    }

    private void showMessage(String message) {
        Toast.makeText(MyApplication.getContext(),message,Toast.LENGTH_LONG).show();
    }


    private void enableInputs(boolean b) {
        if (!b)
        {

            progressBar.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.INVISIBLE);
        }
        else
        {

            progressBar.setVisibility(View.INVISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        }

        this.nameTextView.setEnabled(b);
        this.imageView.setEnabled(b);
    }

    private void populate() {
        pickerImgUri = Uri.parse(user.getUserImage());
        nameTextView.setText(user.getName());
        Glide.with(this).load(user.getUserImage()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                loadProgressBar.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                return false;
            }
            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                loadProgressBar.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            pickerImgUri = data.getData();
            imageView.setImageURI(pickerImgUri);
        }
    }

    private void openGallery () {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void checkandrequestforpermission () {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);

        } else {
            openGallery();
        }
    }
}

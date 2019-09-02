package com.example.b9000application.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.b9000application.Activities.MainActivity;
import com.example.b9000application.Adapters.MyApplication;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.PostRepository;
import com.example.b9000application.Models.ViewModel.PostUpdateViewModel;
import com.example.b9000application.Models.ViewModel.UserViewModel;
import com.example.b9000application.R;

import static android.app.Activity.RESULT_OK;


public class EditPostFragment extends Fragment {

    private TextView tv_Title;
    private TextView tv_SecondTitle;
    private TextView tv_Category;
    private TextView tv_Content;
    private ImageView im_PostImage;
    private Button btn_Edit;
    private PostUpdateViewModel postUpdateViewModel;
    private UserViewModel muserViewModel;
    private ProgressBar progressBar;
    private ConstraintLayout layout;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickerImgUri = null;
    public EditPostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_post, container, false);
        postUpdateViewModel = ViewModelProviders.of(this).get(PostUpdateViewModel.class);
        muserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
       // tv_Category=(Spinner)v.findViewById(R.id.post_edit_category);
        //String CategoryText = tv_Category.getSelectedItem().toString();
        tv_Category = v.findViewById(R.id.post_edit_category);
        tv_Content = v.findViewById(R.id.post_edit_content);
        btn_Edit = v.findViewById(R.id.user_disable_acount_button);
        im_PostImage = v.findViewById(R.id.image_post_post_edit);
        progressBar = v.findViewById(R.id.post_edit_progressBar);
        layout = v.findViewById(R.id.layout_post_edit);
        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.INVISIBLE);
        assert getArguments() != null;
        final Post p = EditPostFragmentArgs.fromBundle(getArguments()).getPost();
        tv_Content.setText(p.getContent());
        //tv_Category.setText(p.getCategory());
        Glide.with(this).load(p.getPicture().toString()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(im_PostImage);
        pickerImgUri = Uri.parse(p.getPicture());
        setupPopupImageClick();
            btn_Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btn_Edit.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    enable_input(false);
                    postUpdateViewModel.ifPostExists(p.getPostKey(), new PostRepository.ExistPostListener() {
                        @Override
                        public void onExist() {
                            if (!tv_Content.getText().toString().isEmpty() && pickerImgUri !=null)
                            {
                                if(pickerImgUri.toString().equals(p.getPicture()))
                                {
                                    p.updatePost(tv_Content.getText().toString(),p.getPicture(),muserViewModel.getUid(),0,muserViewModel.getUserImageUrl().toString(),muserViewModel.getDisplayName());
                                    addPost(p);
                                    enable_input(true);
                                }
                                else
                                {

                                    postUpdateViewModel.saveBlogImage(pickerImgUri, new PostRepository.SaveImageListener() {
                                        @Override
                                        public void onComplete(String imageDownloadLink) {
                                            p.updatePost(tv_Content.getText().toString(),imageDownloadLink,muserViewModel.getUid(),0,muserViewModel.getUserImageUrl().toString(),muserViewModel.getDisplayName());
                                            addPost(p);
                                            enable_input(true);
                                        }

                                        @Override
                                        public void onOffline() {
                                            showMessage("Device not Conected can't upload the post");
                                            btn_Edit.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            enable_input(true);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            showMessage(e.getMessage());
                                            btn_Edit.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            enable_input(true);
                                        }
                                    });

                                }
                            }
                            else
                            {
                                showMessage("Please Verify all input fields and choose post Image");
                                btn_Edit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                enable_input(true);
                            }
                        }

                        @Override
                        public void onNotExist() {
                            showMessage("Post not exist anymore");
                            assert getView() != null;
                            Navigation.findNavController(getView()).navigateUp();
                        }

                        @Override
                        public void onOffline() {
                            showMessage("Device not Conected can't upload the post");
                            btn_Edit.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            enable_input(true);
                        }

                        @Override
                        public void onError(Exception e) {
                            showMessage("Error"+ e.getMessage());
                            enable_input(true);
                        }
                    });


                }
            });
        return v;
    }
    private void showMessage(String message) {
        Toast.makeText(MyApplication.getContext(),message,Toast.LENGTH_LONG).show();
    }

    private void addPost(Post post) {
        postUpdateViewModel.updatePost(post, new PostRepository.InsertPostListener() {
            @Override
            public void onComplete(boolean success) {
                if (success){
                    showMessage("Post Updated successfully");
                    btn_Edit.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    setupPopupImageClick();
                    enable_input(true);
                    assert getView() !=null;
                    Navigation.findNavController(getView()).navigateUp();
                }
            }

            @Override
            public void onError(Exception e) {
                showMessage(e.getMessage());
                btn_Edit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                enable_input(true);
            }

            @Override
            public void onOffline() {
                showMessage("Device not Conected can't upload the post");
                btn_Edit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                enable_input(true);
            }
        });

    }
    private void enable_input(boolean b)
    {

        tv_Content.setEnabled(b);
        im_PostImage.setEnabled(b);

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data!= null)
        {

            pickerImgUri = data.getData();

            im_PostImage.setImageURI(pickerImgUri);

        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }

    private void checkandrequestforpermission() {
        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this.getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);

        }
        else{
            openGallery();
        }
    }

    private void setupPopupImageClick() {

        im_PostImage.setOnClickListener(new View.OnClickListener() {
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and nameTextView
        void onFragmentInteraction(Uri uri);
    }
}

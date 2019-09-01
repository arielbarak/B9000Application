package com.example.b9000application.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.b9000application.Activities.LoginActivity;
import com.example.b9000application.Activities.MainActivity;
import com.example.b9000application.Adapters.MyApplication;
import com.example.b9000application.Adapters.PostAdapter;
import com.example.b9000application.Adapters.PostProfileAdapter;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.User;
import com.example.b9000application.Models.ViewModel.PostsPerUserViewModel;
import com.example.b9000application.Models.ViewModel.PostsPerUserViewModelFactory;
import com.example.b9000application.Models.ViewModel.UserProfileViewModel;
import com.example.b9000application.Models.ViewModel.UserProfileViewModelFactory;
import com.example.b9000application.Models.ViewModel.UserViewModel;
import com.example.b9000application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Button signOutButton;
    Button editProfileButton;
    TextView nameTextView, emailTextView;
    CircleImageView userImageProfile;
    RecyclerView postRecyclerView;
    PostsPerUserViewModel postsPerUserViewModel;
    User user;
    final PostProfileAdapter adapter = new PostProfileAdapter();
    String usedId, userName;
    ConstraintLayout layout;
    ProgressBar progressBar;
    UserProfileViewModel userProfileViewModel;
    UserViewModel userViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View  v = inflater.inflate(R.layout.fragment_profile, container, false);

        signOutButton = v.findViewById(R.id.user_profile_signout_button);
        editProfileButton= v.findViewById(R.id.user_profile_edit_button);
        nameTextView = v.findViewById(R.id.user_profile_name_tv);
        emailTextView = v.findViewById(R.id.user_profile_email_tv);
        userImageProfile = v.findViewById(R.id.user_profile_image);
        postRecyclerView = v.findViewById(R.id.profile_user_post_list);
        layout = v.findViewById(R.id.layout_profile);
        progressBar = v.findViewById(R.id.profile_progressBar);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postRecyclerView.setAdapter(adapter);
        assert getArguments()!=null;
        assert getActivity()!=null;
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        usedId = ProfileFragmentArgs.fromBundle(getArguments()).getUserId();
        if (usedId.equals("profile"))
        {
            usedId = userViewModel.getUid();

        }
        else
        {
            userName = ProfileFragmentArgs.fromBundle(getArguments()).getUserName();
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(userName);
        }
        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(getActivity().getApplication(),usedId);
        userProfileViewModel = ViewModelProviders.of(this,factory).get(UserProfileViewModel.class);
        userProfileViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User u) {
                if(u!=null)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.INVISIBLE);
                    user = u;
                    if(u.getUid().equals(userViewModel.getUid()))
                    {
                    currentUserUpdate();
                    }
                    populate();
                }
            }
        });
        PostsPerUserViewModelFactory pfactory = new PostsPerUserViewModelFactory(getActivity().getApplication(),usedId);
        postsPerUserViewModel = ViewModelProviders.of(this,pfactory).get(PostsPerUserViewModel.class);
                postsPerUserViewModel.getPosts().observe(this, new Observer<LinkedList<Post>>() {


            @Override
            public void onChanged(@Nullable final LinkedList<Post> posts){
                postRecyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                adapter.setPosts(posts);
                postRecyclerView.setAdapter(adapter);
                postRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }


        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivity);
                getActivity().finish();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(EditUserFragmentDirections.actionGlobalEditUserFragment(user));
            }
        });
        return v;
    }

    private void currentUserUpdate() {
        editProfileButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.VISIBLE);
    }


    public void populate()
    {
        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.INVISIBLE);
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        Glide.with(MyApplication.getContext()).load(user.getUserImage()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(userImageProfile);
    }
}

package com.example.b9000application.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.b9000application.Activities.MainActivity;
import com.example.b9000application.Adapters.CommentAdapter;
import com.example.b9000application.Adapters.MyApplication;
import com.example.b9000application.Helper.CommentItemTouchHelper;
import com.example.b9000application.Models.CommentRepository;
import com.example.b9000application.Models.Entities.Comment;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.User;
import com.example.b9000application.Models.PostRepository;
import com.example.b9000application.Models.ViewModel.CommentListViewModel;
import com.example.b9000application.Models.ViewModel.CommentListViewModelFactory;
import com.example.b9000application.Models.ViewModel.PostDetailsViewModel;
import com.example.b9000application.Models.ViewModel.PostDetailsViewModelFactory;
import com.example.b9000application.Models.ViewModel.PostUpdateViewModel;
import com.example.b9000application.Models.ViewModel.UserViewModel;
import com.example.b9000application.R;
import com.google.android.material.snackbar.Snackbar;


import java.util.Date;
import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends Fragment implements CommentItemTouchHelper.CommentItemTouchHelperListener {


    PostDetailsViewModel mPostDetailsViewModel;
    CommentListViewModel mcommentListViewModel;
    UserViewModel mUserViewModel;
    TextView tv_Category,tv_Content,tv_Date_Writer;
    ImageView image_post;
    CircleImageView  image_profile_comment, image_profile;
    Button btn_add_comment;
    PostUpdateViewModel mPostUpdateViewModel;
    EditText et_comment;
    RecyclerView commentRv ;
    ProgressBar progressBar, comment_ProgressBar;
    ConstraintLayout layout;
    CommentAdapter commentAdapter;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack ;
    List<Comment> comments;
    String postKey, userId, currentUserId;

    NestedScrollView rootLayout;
    AlertDialog dialog;

    User u;
    Post p;
    public PostDetailsFragment() {

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actionbar_menu, menu);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id){
            case (R.id.edit_action):
                assert getView() != null;
                Navigation.findNavController(getView()).navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToPostEditFragment3(p));
                break;
            case (R.id.delete_action):
                dialog.show();



        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_post_details, container, false);

        //tv_Category = v.findViewById(R.id.post_d_category);
        tv_Content = v.findViewById(R.id.postContent);
        tv_Date_Writer = v.findViewById(R.id.postWriterDate);
        image_post = v.findViewById(R.id.postImage);
        image_profile_comment = v.findViewById(R.id.userProfileComment);
        image_profile = v.findViewById(R.id.userProfilePost);
        btn_add_comment = v.findViewById(R.id.addCommentBtn);
        comment_ProgressBar = v.findViewById(R.id.comment_progressBar);
        et_comment = v.findViewById(R.id.commentContent);
        layout = v.findViewById(R.id.post_layout);
        progressBar = v.findViewById(R.id.postProgressBar);
        commentRv = v.findViewById(R.id.postCommentRV);
        commentAdapter = new CommentAdapter(getContext());
        rootLayout = v.findViewById(R.id.rootLayout);
        dialog = AskOption();
        //ViewModel And LiveData Init
        assert getArguments() != null;
        postKey = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostKey();
        userId = PostDetailsFragmentArgs.fromBundle(getArguments()).getUserId();
        Activity me = this.getActivity();
        mPostUpdateViewModel = ViewModelProviders.of(this).get(PostUpdateViewModel.class);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        currentUserId = mUserViewModel.getUid();
        PostDetailsViewModelFactory factory;
        CommentListViewModelFactory cFactory;
        if (me != null){
            factory = new PostDetailsViewModelFactory(me.getApplication(),postKey);
            mPostDetailsViewModel = ViewModelProviders.of(this, factory).get(PostDetailsViewModel.class);
            //LiveData Observer Any change is updated
            mPostDetailsViewModel.getPost().observe(this, new Observer<Post>() {
                @Override
                public void onChanged(Post post) {
                    p = post;
                    assert getView() != null;
                    if(p.isDeleted())
                    {
                        
                        Navigation.findNavController(getView()).navigateUp();
                        showMessage("Post deleted!");
                    }
                    if(mUserViewModel.getUid().equals(p.getUserId()))
                        setHasOptionsMenu(true);

                    else
                        setHasOptionsMenu(false);

                    populate();
                }
            });
            cFactory = new CommentListViewModelFactory(me.getApplication(),postKey);
            mcommentListViewModel = ViewModelProviders.of(this,cFactory).get(CommentListViewModel.class);
            me.findViewById(R.id.addPostBtn).setVisibility(View.INVISIBLE);
            mcommentListViewModel.getComments().observe(this, new Observer<List<Comment>>() {
                @Override
                public void onChanged(List<Comment> comments) {
                    commentAdapter.setmData(comments);
                }
            });

        }


        layout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        comment_ProgressBar.setVisibility(View.INVISIBLE);
        btn_add_comment.setVisibility(View.VISIBLE);
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToUserFragment(p.getUserId(),p.getUserName()));
            }
        });

                btn_add_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enable(false);
                        String comment_content = et_comment.getText().toString();
                        String uid = mUserViewModel.getUid();
                        String uname = mUserViewModel.getDisplayName();
                        String uimg = mUserViewModel.getUserImageUrl().toString();
                        if (!comment_content.isEmpty())
                        {
                            Comment c = new Comment(comment_content,p.getPostKey(),uid,uimg,uname);
                            mcommentListViewModel.addComment(c, new CommentRepository.InsertCommentListener() {
                                @Override
                                public void onComplete(boolean success) {
                                    showMessage("Comment added");
                                    et_comment.setText("");
                                    enable(true);
                                    if(getActivity()!=null)
                                    {
                                        ((MainActivity) getActivity()).hideKeyboard();
                                    }
                                    //mcommentListViewModel.notifyChange();
                                }

                                @Override
                                public void onError(Exception e) {
                                    showMessage("Fail to add comment " + e.getMessage());
                                    enable(true);
                                }

                                @Override
                                public void onOffline() {
                                    showMessage("No Internet Connection!");
                                    enable(true);
                                }
                            });
                        }
                        else
                        {
                           enable(true);
                            showMessage("Comment is Empty!");
                        }

                    }
                });

        initRVcomment();


        return v;
    }
    public void enable(boolean flag)
    {
        if(flag)
        {
            comment_ProgressBar.setVisibility(View.INVISIBLE);
            btn_add_comment.setVisibility(View.VISIBLE);
        }
        else
        {
            comment_ProgressBar.setVisibility(View.VISIBLE);
            btn_add_comment.setVisibility(View.INVISIBLE);
        }
        et_comment.setActivated(flag);
    }
    public void populate() {
        if (p != null) {
            layout.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            tv_Content.setText(p.getContent());
            tv_Date_Writer.setText("Published at " +timestamptoString(p.getTimestamp())+ " By " + p.getUserName());
            final Fragment f = this;
            Glide.with(this).load(p.getPicture()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Glide.with(f).load(p.getUserImage()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Glide.with(MyApplication.getContext()).load(mUserViewModel.getUserImageUrl()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    layout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    return false;
                                }
                            }).into(image_profile_comment);
                            return false;
                        }
                    }).into(image_profile);
                    return false;
                }
            }).into(image_post);




        }
    }





    private void loadCurrentUserImage(User user) {
        Glide.with(this).load(user.getUserImage()).into(image_profile_comment);
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    private void initRVcomment() {
        commentRv.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        commentRv.setItemAnimator(new DefaultItemAnimator());
        commentRv.addItemDecoration(new DividerItemDecoration(MyApplication.getContext(), VERTICAL));
        commentRv.setAdapter(commentAdapter);
        itemTouchHelperCallBack = new CommentItemTouchHelper(0, ItemTouchHelper.LEFT,currentUserId, this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(commentRv);

    }


    private AlertDialog AskOption()
    {
        return new AlertDialog.Builder(this.getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to delete the post?")
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog1, int whichButton) {
                        progressBar.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.INVISIBLE);
                        p.setDeleted(true);
                        mPostUpdateViewModel.updatePost(p, new PostRepository.InsertPostListener() {
                            @Override
                            public void onComplete(boolean success) {

                            }

                            @Override
                            public void onError(Exception e) {
                                showMessage(e.getMessage());
                                layout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onOffline() {
                                showMessage("No Internet Connection!");
                                layout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                        dialog1.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {

                        dialog1.dismiss();

                    }
                })
                .create();

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.addPostBtn).setVisibility(View.VISIBLE);
    }
    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.addPostBtn).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String timestamptoString(Date timestamp)
    {

        String date = DateFormat.format("dd-MM-yyyy",timestamp).toString();
        return date;

    }
    private void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof CommentAdapter.MyViewHolder)
        {
            String id = commentAdapter.getItem(viewHolder.getAdapterPosition()).getCommentKey();
            final Comment c = commentAdapter.getItem(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            mcommentListViewModel.removeComment(c, new CommentRepository.RemoveCommentListener() {
                @Override
                public void onRemove() {
                    Snackbar snackbar = Snackbar.make(rootLayout, "Your comment removed!", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mcommentListViewModel.addbackComment(c, new CommentRepository.InsertCommentListener() {
                                @Override
                                public void onComplete(boolean success) {
                                    showMessage("Comment added back");
                                }

                                @Override
                                public void onError(Exception e) {
                                    showMessage("Error  "+ e.getMessage());
                                }

                                @Override
                                public void onOffline() {
                                    showMessage("No Connection!");
                                }
                            });
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }

                @Override
                public void onError(Exception x) {
                    showMessage("Error! "+ x.getMessage());
                    commentAdapter.removeItem(deletedIndex);
                    commentAdapter.restoreItem(c,deletedIndex);
                }

                @Override
                public void onOffline() {
                    showMessage("No Connection!");
                    commentAdapter.removeItem(deletedIndex);
                    commentAdapter.restoreItem(c,deletedIndex);


                }
            });

        }
    }
}

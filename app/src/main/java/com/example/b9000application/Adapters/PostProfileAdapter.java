package com.example.b9000application.Adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.b9000application.Fragments.HomeFragmentDirections;
import com.example.b9000application.Fragments.PostDetailsFragmentDirections;
import com.example.b9000application.Fragments.ProfileFragment;
import com.example.b9000application.Fragments.ProfileFragmentDirections;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.R;

import java.util.LinkedList;

public class PostProfileAdapter extends RecyclerView.Adapter<PostProfileAdapter.MyViewHolder> {

    private LinkedList<Post> mPosts ;

    public PostProfileAdapter() {
        this.mPosts = new LinkedList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_post_item, parent,false);
        
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    public void setPosts(LinkedList<Post> posts) {
        mPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    

    @Override
    public void onBindViewHolder(@NonNull final  MyViewHolder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.layout.setVisibility(View.INVISIBLE);
        final Post post = mPosts.get(position);
        Log.d("Post "+post.getPostKey(), post.getPicture());
        holder.tvWriterName.setText(post.getUserName());
        Glide.with(MyApplication.getContext()).load(post.getUserImage()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Glide.with(MyApplication.getContext()).load(post.getPicture()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.layout.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.layout.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(holder.imgPost);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Glide.with(MyApplication.getContext()).load(post.getPicture()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.layout.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.layout.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(holder.imgPost);
                return false;
            }
        }).into(holder.imgPostProfile);
       // holder.tvTitle.setText(post.getTitle());
       // holder.tvSecondTitle.setText(post.getSecond_title());
        holder.Category.setText(post.getCategory());

    }



    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView Category, tvWriterName;
        ImageView imgPost, imgPostProfile;
        ProgressBar progressBar;
        ConstraintLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_post_item);
            progressBar = itemView.findViewById(R.id.post_item_progress);
            Category = itemView.findViewById(R.id.post_item_category);
            tvWriterName = itemView.findViewById(R.id.post_item_writer_name);
            imgPost = itemView.findViewById(R.id.post_item_image);
            imgPostProfile = itemView.findViewById(R.id.item_user_image_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Navigation.findNavController(v)
                            .navigate(PostDetailsFragmentDirections.actionGlobalPostDetailsFragment(mPosts.get(position).getPostKey(),mPosts.get(position).getUserId()));
                }
            });
        }
    }
}

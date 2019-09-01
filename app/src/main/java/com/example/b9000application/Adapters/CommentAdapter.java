package com.example.b9000application.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.b9000application.Fragments.PostDetailsFragmentDirections;
import com.example.b9000application.Models.Entities.Comment;
import com.example.b9000application.R;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {


        private Context mContext;

    public List<Comment> getmData() {
        return mData;
    }

    public void setmData(List<Comment> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public String getUserId(int position)
    {
        return mData.get(position).getUserId();
    }

    private List<Comment> mData;

        public CommentAdapter(Context mContext) {
            this.mContext = mContext;
            this.mData = Collections.emptyList();
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
            return new MyViewHolder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Comment selected = mData.get(position);
            holder.tvContent.setText(selected.getContent());
            holder.tvWriterName.setText(selected.getUserName());
            Glide.with(mContext).load(selected.getUserImage()).into(holder.imgCommentUserProfile);



        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public Comment getItem(int position)
        {
            return mData.get(position);
        }
        public void removeItem(int position)
        {
            mData.remove(position);
            notifyItemRemoved(position);
        }

        public void restoreItem(Comment comment, int position)
        {
            mData.add(position,comment);
            notifyItemInserted(position);
        }
        public class MyViewHolder extends  RecyclerView.ViewHolder{

            public TextView tvContent,tvWriterName;
            public ImageView imgCommentUserProfile;
            public RelativeLayout viewBackground, viewForeground;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvContent = itemView.findViewById(R.id.comment_item_content);
                tvWriterName = itemView.findViewById(R.id.comment_item_user);
                imgCommentUserProfile = itemView.findViewById(R.id.item_user_image_card);
                viewBackground = itemView.findViewById(R.id.comment_view_background);
                viewForeground = itemView.findViewById(R.id.comment_view_foreground);

                imgCommentUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Navigation.findNavController(view).navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToUserFragment(mData.get(position).getUserId(),mData.get(position).getUserName()));
                    }
                });
            }

            public String getUid()
            {
                return mData.get(this.getAdapterPosition()).getUserId();
            }

            public void show()
            {
                this.itemView.refreshDrawableState();
            }
        }


}

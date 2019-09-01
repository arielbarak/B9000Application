package com.example.b9000application.Models.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@Entity(tableName = "comment_table", indices = {@Index("userId"), @Index("postId")})
public class Comment {
    @NonNull
    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(@NonNull String commentKey) {
        this.commentKey = commentKey;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "commentKey")
    private String commentKey = "";
    @NonNull
    private String content="";
    @NonNull
    private String userId="";
    @NonNull
    private String userImage="";
    @NonNull
    private String userName="";

    @NonNull
    private String postId ="";
    @NonNull
    @ServerTimestamp private Date timestamp = new Date();

    @NonNull
    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }




    public Comment(@NonNull String content,@NonNull String postId,@NonNull String userId,@NonNull String userImage,@NonNull String userName) {
        this.content = content;
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.postId = postId;
    }

    @Ignore
    public Comment() {
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public void setUserImage(@NonNull String userImage) {
        this.userImage = userImage;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public @NonNull String getContent() {
        return content;
    }

    public @NonNull String getUserId() {
        return userId;
    }

    public @NonNull String getUserImage() {
        return userImage;
    }

    public @NonNull String getUserName() {
        return userName;
    }

    public @NonNull Date getTimestamp() {
        return timestamp;
    }
}

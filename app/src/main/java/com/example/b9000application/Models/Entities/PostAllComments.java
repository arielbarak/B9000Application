package com.example.b9000application.Models.Entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PostAllComments {
    @Embedded
    Post post;
    @Relation(parentColumn = "id", entityColumn = "postId", entity = Comment.class)
    public List<Comment> comments;

    public PostAllComments(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}



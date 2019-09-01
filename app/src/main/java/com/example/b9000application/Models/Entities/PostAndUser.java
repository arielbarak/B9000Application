package com.example.b9000application.Models.Entities;

import androidx.room.Embedded;

import java.util.ArrayList;
import java.util.List;

public class PostAndUser {
    @Embedded
    public User user;
    @Embedded
    public Post post;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PostAndUser(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public static List<Post> toPosts(List<PostAndUser> list)
    {
        List<Post> plist = new ArrayList<>();
        for (PostAndUser p: list             ) {
            plist.add(p.getPost());
        }
        return plist;
    }


}

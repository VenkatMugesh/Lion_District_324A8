package com.example.leodistrict324a8.Model;

public class Post {

    String club , title , desc , imageUrl , postId , publisher;

    public Post(){}

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Post(String club, String title, String desc, String imageUrl, String postId, String publisher) {
        this.club = club;
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publisher = publisher;
    }
}

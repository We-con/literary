package com.reader.readingManagement.user.model;

/**
 * Created by Harry Kim on 2017. 2. 23..
 */
public class UserInfo {
    String id;
    String profileImageUrl;
    String nickname;
    String name;
    String email;
    public UserInfo(String id, String nickname, String url) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = url;
    }

    public UserInfo(String id, String nickname, String url, String name, String email) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = url;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

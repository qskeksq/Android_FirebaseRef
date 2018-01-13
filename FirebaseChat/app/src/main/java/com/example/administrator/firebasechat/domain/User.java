package com.example.administrator.firebasechat.domain;

/**
 * 유저 정보
 */
public class User {

    private String email;
    private String password;
    private String name;
    private String photo;
    private String key;
    private boolean isChecked = false;      // 채팅방 만들 때 체크 여부

    /**
     * default 생성자
     */
    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}

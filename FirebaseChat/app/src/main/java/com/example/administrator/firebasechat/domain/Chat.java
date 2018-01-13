package com.example.administrator.firebasechat.domain;

/**
 * send 한 번 할 때 만들어지는 채팅 정보
 */
public class Chat {

    private String email;       // 자신과 나머지 사람 구분
    private String content;

    /**
     * 파이어베이스 위해 반드시 default 생성자 필요
     */
    public Chat() {

    }

    public Chat(String email, String content) {
        this.email = email;
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

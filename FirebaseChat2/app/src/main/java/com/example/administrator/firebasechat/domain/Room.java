package com.example.administrator.firebasechat.domain;

/**
 * 채팅방 정보 - 방은 이름으로만 구분해 준다.
 */
public class Room {

    private String name;

    /**
     * default 생성자
     */
    public Room() {

    }

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

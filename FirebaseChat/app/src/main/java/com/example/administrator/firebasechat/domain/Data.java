package com.example.administrator.firebasechat.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 한 번 로그인 할 때 임시 저장소
 * 1. 현재 사용자 정보
 * 2. 전체 사용자 정보
 * 3. 방 정보
 */
public class Data {

    /**
     * 현재 사용자 정보
     */
    private static User user;

    /**
     * 채팅방 정보
     */
    public static List<Room> rooms = new ArrayList<>();

    /**
     * 전체 사용자 정보
     */
    public static List<User> users = new ArrayList<>();


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Data.user = user;
    }


}

package com.example.administrator.firebasechat.Util;

/**
 * Created by Administrator on 2017-07-20.
 */

public class ManageComma {

    /**
     * 메일을 키 값으로 저장하기 위한 치환
     * @param email
     * @return
     */
    public static String replaceComma(String email){
        return email.replace(".", "_comma_");
    }

    public static String recoverComma(String email){
        return email.replace("_comma_", ".");
    }

}

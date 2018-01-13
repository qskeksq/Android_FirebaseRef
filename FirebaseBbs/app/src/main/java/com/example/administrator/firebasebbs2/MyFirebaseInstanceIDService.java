package com.example.administrator.firebasebbs2;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    // 내 스마트폰을 인식하는 토큰이 변경되면 시스템에서 자동으로 호출
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        // 토큰을 서버에 직접 업로드해서 업데이트한다.
    }
}

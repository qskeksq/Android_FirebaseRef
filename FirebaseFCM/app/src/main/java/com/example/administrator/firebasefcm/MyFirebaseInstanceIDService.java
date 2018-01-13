package com.example.administrator.firebasefcm;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.example.administrator.firebasefcm.MainActivity.data;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("uid");

    Uid uid;

    public MyFirebaseInstanceIDService() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // 당연히 생성자에서 하니까 문제가 생기지 생성한 적이 없는데 갖다가 쓸려고 하니까
        String deviceUid = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.e("UUID", deviceUid);
        uid = new Uid();
        uid.deviceUid = deviceUid;
        uid.name = "손님";
        uid.token = refreshedToken;

        data.add(uid);


        myRef.child(deviceUid).setValue(uid);


    }


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();


    }


}

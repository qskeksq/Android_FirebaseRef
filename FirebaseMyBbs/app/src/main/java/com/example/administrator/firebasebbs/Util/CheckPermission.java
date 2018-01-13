package com.example.administrator.firebasebbs.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;


public class CheckPermission {

    public static final int REQ_CODE = 999;
    private static String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void checkVersion(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            customCheckPermission(activity);
        } else {
            callInit(activity);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void customCheckPermission(Activity activity){
        boolean denied = false;
        for(String per : perms){
            if(activity.checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED){
                denied = true;
                break;
            }
        }

        if(denied){
            activity.requestPermissions(perms, REQ_CODE);
        } else {
            callInit(activity);
        }

    }

    public static void onResult(int requestCode, int[] grantResult, Activity activity){
        if(requestCode == REQ_CODE) {
            boolean granted = true;
            for (int grant : grantResult) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                callInit(activity);
            } else {
                Toast.makeText(activity, "권한 설정을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public interface CallBack {
        void callInit();
    }

    public static void callInit(Activity activity){
        ((CallBack)activity).callInit();
    }


}

package com.example.administrator.firebasebbs2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.firebasebbs2.Util.CheckPermission;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements CheckPermission.CallBack {

    // 1. 인증 처리 관련은 -- oncreate 보다 먼저 하는 곳에
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // 1.
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("Auth", "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };

    EditText editEmail, editPassword, inEmail, inPass, inName;
    Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckPermission.checkVersion(this);

    }

    // 2.
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    // 2.
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void goList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPermission.onResult(requestCode, grantResults, this);
    }

    @Override
        public void callInit() {
        editEmail = (EditText) findViewById(R.id.id);
        editPassword = (EditText) findViewById(R.id.password);
        inEmail = (EditText) findViewById(R.id.signupemail);
        inPass = (EditText) findViewById(R.id.signuppassword);
        inName = (EditText) findViewById(R.id.signupname) ;
        Log.e("확인", "뜨는지 확인");
    }

    // 3.
    public void signup(View view){

        // 메일이 gmail 이어야지 성공할 수 있다. 이게 뭐냐
        String email = inEmail.getText().toString();
        String password = inPass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Auth", "createUserWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w("Auth", "signInWithEmail:failed", task.getException());
                        Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "성공", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    // 4.
    public void signin(View view){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Auth", "signInWithEmail:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.w("Auth", "signInWithEmail:failed", task.getException());
                        Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "성공", Toast.LENGTH_SHORT).show();
                        goMain();
                    }
                }
            });
    }

    public void goMain(){

        Intent intent = new Intent(this, NaviActivity.class);
        startActivity(intent);
        finish();

    }

}

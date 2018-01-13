package com.example.administrator.firebasebbs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.firebasebbs.Util.CheckPermission;
import com.example.administrator.firebasebbs.domain.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements CheckPermission.CallBack{

    String email, password, name;
    EditText editEmail, editPassword, inEmail, inPass, inName;
    private final String TAG = this.getClass().getSimpleName();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("user");

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        }
    };

    /**
     * 생명주기를 통해 인증 관리
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CheckPermission.checkVersion(this);
    }

    /**
     * 회원가입
     */
    public void signup(View view) {
         email = inEmail.getText().toString();
         password = inPass.getText().toString();
         name = inName.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "성공", Toast.LENGTH_SHORT).show();
                            setValue(name, email, password);
                        }
                    }
                });
    }

    /**
     * 로그인
     */
    public void signin(View view) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        Log.e("로그인 확인", "첵");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "성공", Toast.LENGTH_SHORT).show();
                            goMain();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPermission.onResult(requestCode,grantResults,this);
    }

    @Override
    public void callInit() {
        editEmail = (EditText) findViewById(R.id.id);
        editPassword = (EditText) findViewById(R.id.password);
        inEmail = (EditText) findViewById(R.id.signupemail);
        inPass = (EditText) findViewById(R.id.signuppassword);
        inName = (EditText) findViewById(R.id.signupname) ;
    }

    public void goMain(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 회원가입할 때 가입자 정보를 데이터베이스에 저장한다.
     */
    public void setValue(String name, String email, String password){
        User user = new User(name, email, password);
        String childKey = replaceComma(user.getEmail());
        reference.child(childKey).setValue(user);
    }

    public String replaceComma(String email){
        return email.replace(".", "_comma_");
    }

}

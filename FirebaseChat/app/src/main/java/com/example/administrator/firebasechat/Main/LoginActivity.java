package com.example.administrator.firebasechat.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.Util.ManageComma;
import com.example.administrator.firebasechat.domain.Data;
import com.example.administrator.firebasechat.domain.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataReference = database.getReference("users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            // 로그인을 할 떄 유저가 있는지 없는지 확인
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };

    /**
     * 유저 & 로그인 정보
     */
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText loginEmail;
    private EditText loginPass;

    /**
     * 로그인 정보 임시 저장
     */
    SharedPreferences.Editor editor;
    SharedPreferences sp;

    /**
     * 인증 리스너 등록 / 임시 저장 데이터 출력
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        String email = sp.getString("email", "");
        String pass = sp.getString("pass", "");
        loginEmail.setText(email);
        loginPass.setText(pass);
    }

    /**
     * 인증 리스너 해제
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPass = (EditText) findViewById(R.id.loginPass);
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 회원가입
     */
    public void signUp(View view) {
        final String email = loginEmail.getText().toString();
        final String password = loginPass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 가입 실패
                        if (!task.isSuccessful()) {
                            if (user.getEmail().equals(email)) {
                                Toast.makeText(LoginActivity.this, "이미 가입되어 있습니다", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "가입 실패", Toast.LENGTH_SHORT).show();
                            }
                            // 가입 성공
                        } else {
                            Toast.makeText(LoginActivity.this, "가입 성공", Toast.LENGTH_SHORT).show();
                            // 가입 정보 서버에 저장
                            User user = new User(email, password);
                            String keyByMail = ManageComma.replaceComma(user.getEmail());
                            user.setKey(keyByMail);
                            dataReference.child(keyByMail).setValue(user);
                        }
                    }
                });
    }


    /**
     * 로그인
     */
    public void signIn(View view) {
        final String email = loginEmail.getText().toString();
        final String password = loginPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 정보 처리 후 로그인 된 유저 정보를 임시 저장소에 저장
                            setProfileData(email);
                            Toast.makeText(LoginActivity.this, "환영합니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // 로그인 된 아이디와 비밀번호 저장
        editor.putString("email", email);
        editor.putString("pass", password);
        editor.commit();
    }

    /**
     * 로그인 할 때 사용자 정보를 임시저장소에 저장
     */
    public void setProfileData(final String email){
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 유저 임시 정보 저장소 clear
                Data.users.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    // 전체 유저 반환
                    Data.users.add(item.getValue(User.class));
                    // 본인 반환
                    String key = item.getKey();
                    if(email.equals(ManageComma.recoverComma(key))){
                        User user = item.getValue(User.class);
                        Data.setUser(user);     // 데이터 세팅이 끝나지 않고 goMain 을 호출하게 되면, 위의 경우 setProfileData -> goMain 프로필이 서버에서 다
                                                // 오지 않은 상태에서 넘어가고, 다음 페이지에서 프로필 정보를 사용해서 띄우는 경우 null 값이 뜬다.
                        // goMain 하더라도 데이터는 계속 채워진다. todo 다만 이렇게 하면 back 버튼 누를 때 로그인 창으로 돌아가 버린다.
                        goMain();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 자바에 대한 이해가 없어서 생긴 현상이다. 일단 메모리 누수 현상에 대해 잘 찾아보고, 디버깅 툴 이용해서 생명주기 관리, 메모리 참고, GC 가
    // 어떻게 동작하는지 공부한다. 즉, 익명 객체로 생성한 것은 다른 객체이기 때문에 액티비티가 없어져도 참조가 사라지지 않고 계속 있다가
    // 참조가 쌓이게 되는 듯 하다. 또한 레퍼런스에 addValueListener 을 보면 add 해 주는 것으로 리스너를 더해준다는 것을 알 수 있다.
    // 어디서든지 static 으로 만들어진 파이어베이스 레퍼런스는 메모리에 올라가 있는데, 거기에 add 리스너를 했으니 참조는 죽지 않고 어디서든지
    // 계속 참조하는 것이다. 즉,참조가 죽든지 말든지, 어떻게 동작하는지, 왜 new 가 아니라   FirebaseAuth.getInstance().getCurrentUser(); 이렇게
    // 해도 데이터가 가져와 지는지, 메모리는 언제 죽고 언제 쌓이는지, 하나 액티비티, 하나 프래그먼트, 하나 인플레이션, 한 메모리 영역
    // 손댈 떄마다 이 뷰, 액티비티, 프래그먼트, 객체의 생명주기를 파악해야 하는 것이다.


    // 이떄까지는 구현되는 것에만 집중했다면 이제는 생명주기에서 더 나아가 메모리, 하드웨어 사고까지 해야 하고, 언어 자체에 대한 공부
    // 도 더 해야 하는 것이다. 또한 디자인 패턴을 공부해서 각각이 어떻게 구현되있고, 어떤 패턴으로 만들어지고 죽는지 파악해야 한다.

    // 그리고 문제가 생기면 문제를 파악해 나가는 패턴도 있어야 한다. 일단 문제가 생기는 영역을 파악했어야 한다. 여기서는 특정 액티비티가
    // 계속 생겨났으면 1. 그 액티비티 2. 그리고 그 액티비티가 만들어 지는 곳으로 찾아갔어야지

    // 내가 쓰고 있는 것의 패턴과 그 내부를 이해하지 않고 단순하게 가져오고 보여주고만 하면 정말 큰일난다. 디버깅은 일단 무조건 못 하게 된다.

    // 파이어베이스를 사용하기 전에 먼저 문서를 읽어보고 구동 방식을 좀 이해하고 사용해라. 문서를 읽어보든지 그 안에 들어가서 다 읽어보든지
    // 디버깅을 잘 읽어보든지 해라. 파이어베이스의 교훈.

    // 하나 만들 때는 완결성이 있어야 한다. 즉, 시작부터 메모리에서 제거되는 순간까지 책임져 줘야 하는 것이다.

    // 테스트 주도, 비더깅 개발에 대한 내용은 공부하자

    // 디버깅 공부

    // 6개월정도 하다보면 이해가 깊어지고, 그 후부터는 다른 언어들을 찾아보면서 서베이식으로 공부하다 보면 프로그래밍에 대한 이해가 생긴다
    // 공부는 이렇게 해야 하는 것이다.

    // 참고로 액티비티에서 프래그먼트에 정보 전달하는 것은 인터페이스 사용하면 된다.

    /**
     * 메인 페이지로 이동
     */
    public void goMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}

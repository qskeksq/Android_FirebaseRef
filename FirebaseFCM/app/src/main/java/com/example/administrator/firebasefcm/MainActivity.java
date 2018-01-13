package com.example.administrator.firebasefcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity implements CustomAdapter.IAdapter{

    // 파이어베이스 데이터베이스 연결


    public static List<Uid> data = new ArrayList<>();

    TextView textView;
    EditText editText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 먼저 서비스를 만들어 줘야 함

        //토큰 확인 작업
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.e("Message", "Refreshed token: " + refreshedToken);

        // 1. uuid 생성 후 객체에 담아주기
        // 모든 디바이스는 uuid, 절대 변하지 않는 핸드폰 식별자를 가지고 있다.



        // 2. 데이터베이스에 저장 -- inner 클래스를 인식을 못 함



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec);

        CustomAdapter customAdapter = new CustomAdapter(data, this);

        recyclerView.setAdapter(customAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        textView = (TextView) findViewById(R.id.textView);



    }

    @Override
    public void setToken(String token) {
        textView.setText(token);
    }

    String deviceid;
//
//    public void send(View view){
//
//        String temp = textView.getText().toString();
//
//        myRef.child(deviceid).setValue();
//
//    }

    //------------레트로핏을 사용하기 위한 서버스 선언부---------------------//
    // 1.
    public interface FcmService {
        public static final String SERVER_URL = "http://192.168.10.240:8080/";

        @POST("send_notification")  // SERVER_URL 과 합쳐
            // 받을 결과값의 타입 // 전송할 데이터 타입
        Call<Result> sendFcm(@Body Msg data);
    }

    // 2.
    public class Msg {
        String token;
        String msg;
    }

    // 3.
    public class Result {
        String result_status;
    }


    public void send(View view){

        String token = textView.getText().toString();
        String msg = editText.getText().toString();


        // 1. 레트로핏 주소를 가지고 있는 인터페이스를 정의한다.
        // 2. 전송값을 전달할 클래스를 정의한다
        // 3. 결과값을 받을 클래스를 정의한다.
        // 4. Retrofit 클래스를 사용하기 위해 build 한다.

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FcmService.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())      // 빼먹지 말 것!!!
                .build();

        // 5. 전송에 사용될 인터페이스를 초기화 한다.
        FcmService service = retrofit.create(FcmService.class);

        // 6. 전송할  클래스에 값을 담는다.
        Msg data = new Msg();
        data.token = token;
        data.msg = msg;

        // 7. 인터페이스 중에 사용할 메서드를 선택한다
        Call<Result> result = service.sendFcm(data);

        // 8. 서브스레드에서 실행
        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful()){
                    Result data = response.body();
                    Log.e("결과값 확인", data.result_status);
                } else {
                    Log.e("결과값 확인", response.message());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });


    }



//    public void send(View view){
//
//        String token = textView.getText().toString();
//        String msg = editText.getText().toString();
//
//        String serverUrl = "http://192.168.10.85:8080/send_notification";
//
//        try {
//            URL url = new URL(serverUrl);
//
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//            con.setRequestMethod("POST");
//
//            OutputStream os = con.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
//            writer.write(temp);
//
//        } catch (Exception e){
//
//            e.printStackTrace();
//        }
//
//
//    }

    // 일단 왜 해 주는가?
    // 로그인 기능이 없는 앱의 경우에도 그 디바이스가 사용하고 있음을 식별해야 한다.
    // 한마디로 로그인하지 않는 회원을 관리하려고 하는군.
    // 그리고 나중에 로그인을 하거나 가입을 하면 UUID 를 이제 ID 로 바꿔주는 것이겠군
    // 일단







}

class Uid {

    String deviceUid;
    String name;
    String token;

}

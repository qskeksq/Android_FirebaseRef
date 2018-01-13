package com.example.administrator.firebasebbs2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.firebasebbs2.domain.Bbs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class WriteActivity extends AppCompatActivity {

    EditText editTitle, editAuthor, editContent;
    FirebaseDatabase database;      // json 데이터 저장하는 곳
    DatabaseReference reference;
    StorageReference storageReference;      // 사진 저장하는 곳

    TextView textView;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        editTitle = (EditText) findViewById(R.id.title);
        editAuthor = (EditText) findViewById(R.id.author);
        editContent = (EditText) findViewById(R.id.content);
        textView = (TextView) findViewById(R.id.filename);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("bbs");
        storageReference = FirebaseStorage.getInstance().getReference("images"); // 원래 images/rivers.jpg 에 있던 건데 디렉토리로 뺴 준 것임

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void uploadFile(String filepath) {

        // 스마트폰에 있는 파일 경로 -- 이렇게 분리해주는 이유는 쉽게 이름이나 값을 분리해 낼 수 있기 때문
        File file = new File(filepath);
        Uri uri = Uri.fromFile(new File(filepath));

        // 파이어베이스에 있는 파일 경로
        String filename = file.getName(); // + 시간값 or UUID 추가해서 만듦.
        // 데이터베이스의 키가 값과 동일한 구조임.
        StorageReference filesRef = storageReference.child(filename);   // 파일명으로 된 빈 껍데기 파일을 하나 만들어주고

        // 파일 업로드
        filesRef.putFile(uri)                                           // putFile 에서 데이터를 넣어주는 구조다.
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // 파이어베이스 스토리지의 저장된 방금 업로드한 파일의 경로
                    @SuppressWarnings("VisibleForTests")
                    Uri uploadedUrl = taskSnapshot.getDownloadUrl();
                    afterUploadFile(uploadedUrl);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    // ...
                }
            });

    }

    // 1. 가장 먼저 이미지가 있는지 확인하고 업로드
    public void post(View view){
        dialog.show();
        String imagePath = textView.getText().toString();
        if(imagePath != null && !"".equals(imagePath)){
            uploadFile(imagePath);
            Log.e("uploadFile", "여긴가");
        } else {
            afterUploadFile(null);  // 아하 ! null 을 이렇게 처리해 줄 수도 있구나
            Log.e("afterUploadFile", "아니면 여긴가");
        }
    }

    // 2. 업로드
    public void afterUploadFile(Uri imgUri) {
        String title = editTitle.getText().toString();
        String author = editAuthor.getText().toString();
        String content = editContent.getText().toString();

        // 파이어베이스 데이터베이스에 데이터 넣기
        // 1. 데이터 객체 생성
        Bbs bbs = new Bbs(title, author, content);

        if(imgUri != null){
            bbs.setFileUriString(imgUri.toString());
        }

        // 2. 입력할 데이터의 키 생성
        String bbsKey = reference.push().getKey();  // push 를 통해 자동 생성된 키를 가져온다

        // 3. 생성된 키를 레퍼런스로 데이터를 입력
        //      insert, update 는 동일하게 동작
        reference.child(bbsKey).setValue(bbs);

//        // 삭제는
//        reference.child(bbsKey).removeValue();
//        reference.child(bbsKey).setValue(null);

        dialog.dismiss();
        finish();

    }

//--------------------------------------------------------------------------------------------------
//    이미지 가져오기
//--------------------------------------------------------------------------------------------------

    public void openGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 가. 이미지 선택창 호출
        startActivityForResult(Intent.createChooser(intent, "select photo"), 100);    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                // 나. 이미지 선택창에서 선택된 이미지의 경로 추출
                case 100:
                    Uri imageUri = data.getData();
//                    File file = new File(imageUri.getPath());       // filename 을 알아야 할 경우 이렇게 사용한다. 보통은 파일 이름으로 가져온다.
                    String filepath = getPathFromUri(this, imageUri);
                    textView.setText(filepath);
                    break;
            }
        }
    }

    // 앞으로도 그냥 UTIL 성으로 복붙하면 됨.
    private String getPathFromUri(Context context, Uri uri){
        String realPath = "";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if(cursor.moveToNext()){
            realPath = cursor.getString(cursor.getColumnIndex("_data"));        // 이것은 지정되어 있는 것으로 진짜 경로를 path 를 가져올 수 있다.
        }
        cursor.close();
        return realPath;
    }

}

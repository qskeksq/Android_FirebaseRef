package com.example.administrator.firebasebbs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.firebasebbs.domain.Bbs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class CreateActivity extends AppCompatActivity {

    TextView filename;
    EditText editTitle, editAuthor, editContent;
    ProgressDialog dialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storageDatabase = FirebaseStorage.getInstance();
    DatabaseReference reference = database.getReference("bbs");
    StorageReference storageReference = storageDatabase.getReference("images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initViews();
    }

    private void initViews(){
        editTitle = (EditText) findViewById(R.id.title);
        editAuthor = (EditText) findViewById(R.id.author);
        editContent = (EditText) findViewById(R.id.content);
        filename = (TextView) findViewById(R.id.filename);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    // 1. 사진 데이터 있으면 가져오기
    public void openGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 999);
    }

    // 2. 사진 데이터 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 999){
                Uri imageUri = data.getData();
                // 데이터가 있는지 확인한다. 다만 내가 원하는 데이터는 Uri 의 직접 값이 아니라 경로이기 때문에 경로를 구하는 메소드를 만들어 준다.
                filename.setText(getPathFromUri(imageUri));
            }
        }
    }

    private String getPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null,null,null,null,null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return path;
    }

    // 3. 서버에 데이터 보내주기
    public void post(View view){
        dialog.show();
        // 1. 이미지 파일이 있으면 처리해준다. -- 데이터 저장소가 다르기 때문에 처리를 따로 해준다.
        String filepath = filename.getText().toString();
        if(!"".equals(filepath) && filepath != null){
            postPhoto(filepath);
            // 이미지가 없으면 바로 나머지 데이터만 저장한다.
        } else {
            postData(null);
        }
    }

    // 3.1 서버에 이미지 파일 올려줄 메소드
    private void postPhoto(String filepath){
        // 파일로 넣어줄 것이기 때문에 경로로 파일을 만들어 주고
        File file = new File(filepath);
        // 이미지가 들어갈 곳의 키 값을 만들어 주고
        String filename = file.getName();
        // 위에서 존재하는 images 키 값 아래 다시 이미지 파일명으로 사진 경로가 들어갈 곳의 키 값을 설정해 준다.
        StorageReference photoRef = storageReference.child(filename);
        // 이미지 Uri -> 경로추출 String 값으로 변환-> 경로를 통해 File 객체 생성 -> File 을 통해 Uri 생성 -> 스마트폰에서 Uri 로 접근하여 서버로 이미지를 전송
        photoRef.putFile(Uri.fromFile(file))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        // 데이터 객체에 저장할 이미지 Uri 값 만들어준다.
                        Uri uploadedUri = taskSnapshot.getDownloadUrl();
                        Toast.makeText(CreateActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                        postData(uploadedUri);
                    }
                });
    }

    // 3.2 사진 이외의 텍스트 데이터를 객체화 해서 서버에 저장하기
    private void postData(Uri uri){
        // 1. 서버에 올릴 데이터를 객체로 만들어 주고
        String title = editTitle.getText().toString();
        String author = editAuthor.getText().toString();
        String content = editContent.getText().toString();
        Bbs bbs = new Bbs(title,author,content);
        if(uri != null) {
            bbs.setFileUriString(uri.toString());
        }

        // 2. 데이터를 조회할 저장 & 조회할 키 값
        String key = reference.push().getKey();
        bbs.setBbsKey(key);

        // 3. 키값으로 저장
        reference.child(key).setValue(bbs);
        Toast.makeText(CreateActivity.this, "데이터 업로드 성공", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        finish();
    }
}

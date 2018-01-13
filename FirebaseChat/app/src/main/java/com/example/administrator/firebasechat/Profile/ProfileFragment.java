package com.example.administrator.firebasechat.Profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.domain.Data;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * 프로필 관리
 */
public class ProfileFragment extends Fragment {

    /**
     * 파이어베이스 데이터베이스, 레퍼런스
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference databaseReference = database.getReference("users");
    private StorageReference storageReference = storage.getReference("users");

    private ImageView profileImage;
    private TextView profileMail;
    private TextView profileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        setListener();
        return view;
    }

    /**
     * 초기화
     */
    private void initView(View view) {
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        profileMail = (TextView) view.findViewById(R.id.profileMail);
        profileName = (TextView) view.findViewById(R.id.profileName);
        // 처음 화면이 뜰 때 기본 값 세팅
        profileMail.setText(Data.getUser().getEmail());
        profileName.setText(Data.getUser().getPassword());
        Glide.with(getActivity()).load(Data.getUser().getPhoto()).placeholder(R.mipmap.ic_launcher_round).into(profileImage);
    }

    /**
     * 이미지 가져오기
     */
    private void setListener(){
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 999);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 999){
                final Uri imgUri = data.getData();
                storageReference.child(Data.getUser().getEmail()).putFile(imgUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests")
                                // 사진이 저장될 Url
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                // 사진이 선택되면 user 정보에 프로필 사진 url 추가
                                databaseReference.child(Data.getUser().getKey()).child("photo").setValue(downloadUrl.toString());
                                Glide.with(getActivity()).load(imgUri).placeholder(R.mipmap.ic_launcher_round).into(profileImage);
                                Toast.makeText(getActivity(), "프로필이 설정되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}

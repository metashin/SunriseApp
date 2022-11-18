package com.example.sunriseapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MemberActivity extends AppCompatActivity {

    private String TAG = "MemberInitActivity";

    private ImageView profileImageView;
    private String profilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);

        findViewById(R.id.configButton).setOnClickListener(onClickListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data ){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    profilePath = data.getStringExtra("profilePath");
                    Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                    profileImageView.setImageBitmap(bmp);

                    //Glide.with(this).load(profilePath).centerCrop().override(500).into(profileImageView);

                }
                break;
        }
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.configButton:
                    updateMemberInfo();
                    break;
                case R.id.profileImageView:
                    Log.e("카메라버튼","클릭");
                    moveActivity(CameraActivity.class);
                    break;
            }
        }
    };

    private void updateMemberInfo(){
        final String name = ((EditText)findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birthDate = ((EditText)findViewById(R.id.birthDateEditText)).getText().toString();
        final String address = ((EditText)findViewById(R.id.addressEditText)).getText().toString();

        if(name.length() > 0 && phoneNumber.length() > 9 && birthDate.length() > 5 && address.length() > 0){

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            //String fileDate = dateFormat.format(new Date());
            //final StorageReference mountainImagesRef = storageRef.child("images/"+user.getUid()+"/"+fileDate+"profileImage.jpg");

            final StorageReference mountainImagesRef = storageRef.child("images/"+user.getUid()+"/"+"profileImage.jpg");

            try {
                InputStream stream = new FileInputStream(new File(profilePath));
                UploadTask uploadTask = mountainImagesRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("실패","fail");
                            throw task.getException();
                        }
                        return mountainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.e("성공","Success :"+downloadUri);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            MemberInfo member = new MemberInfo(name, phoneNumber, birthDate,address,downloadUri.toString());

                            db.collection("users").document(user.getUid()).set(member);
                            DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                            washingtonRef
                                    .update("capital", true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startToast("회원정보 등록을 성공하였습니다.");
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            startToast("회원정보 등록을 실패하였습니다.");
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        } else {
                            Log.e("실패","fail");
                        }
                    }
                });
            }catch (FileNotFoundException e){
                Log.e("에러 :",e.toString());
            }
        }else{
            startToast("회원정보를 입력해주세요");
        }


    }
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void moveActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);

    }

}
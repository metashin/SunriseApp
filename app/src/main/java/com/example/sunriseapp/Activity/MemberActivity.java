package com.example.sunriseapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sunriseapp.MemberInfo;
import com.example.sunriseapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);

        findViewById(R.id.configButton).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);
        findViewById(R.id.cappicture).setOnClickListener(onClickListener);
        findViewById(R.id.profileImageView).setOnClickListener(onClickListener);
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
                    Log.e("log","click");
                    CardView cardView = findViewById(R.id.caneraCard);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.gallery:
                    if(ContextCompat.checkSelfPermission(MemberActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        Log.e("permission","??????");
                        if(ActivityCompat.shouldShowRequestPermissionRationale(MemberActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)){
                            ActivityCompat.requestPermissions(MemberActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);
                            Log.e("permission","??????");
                        }else{
                            ActivityCompat.requestPermissions(MemberActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);
                            startToast("????????? ????????? ?????????.");
                            Log.e("permission","??????");
                        }
                    }else{
                        moveActivity(GalleryActivity.class);
                    }

                    break;
                case R.id.cappicture:
                    moveActivity(CameraActivity.class);
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    moveActivity(GalleryActivity.class);
                } else {
                    startToast("????????? ????????? ?????????.");
                }
            }
            break;
            default:
                break;
        }
    }

    private void updateMemberInfo(){
        final String name = ((EditText)findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birthDate = ((EditText)findViewById(R.id.birthDateEditText)).getText().toString();
        final String address = ((EditText)findViewById(R.id.addressEditText)).getText().toString();

        if(name.length() > 0 && phoneNumber.length() > 9 && birthDate.length() > 5 && address.length() > 0){

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();

            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            //String fileDate = dateFormat.format(new Date());
            //final StorageReference mountainImagesRef = storageRef.child("images/"+user.getUid()+"/"+fileDate+"profileImage.jpg");

            final StorageReference mountainImagesRef = storageRef.child("images/"+user.getUid()+"/"+"profileImage.jpg");

            if(profilePath == null){
                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDate,address);
                uploader(memberInfo);
                Log.e("??????","4??? ?????? ??????"+profilePath);
            }else{
                try {
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.e("??????","fail");
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDate,address,downloadUri.toString());
                                uploader(memberInfo);
                                Log.e("??????","????????? ?????? ??????");
                            } else {
                                startToast("??????????????? ???????????? ??????????????????.");
                            }
                        }
                    });
                }catch (FileNotFoundException e){
                    Log.e("?????? :",e.toString());
                }
            }

        }else{
            startToast("??????????????? ??????????????????");
        }


    }

    private void uploader(MemberInfo member){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(user.getUid()).set(member);
        DocumentReference washingtonRef = db.collection("users").document(user.getUid());
        washingtonRef
                .update("capital", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("???????????? ????????? ?????????????????????.");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("???????????? ????????? ?????????????????????.");
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void moveActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);

    }

}
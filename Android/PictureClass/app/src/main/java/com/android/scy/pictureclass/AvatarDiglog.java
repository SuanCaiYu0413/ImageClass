package com.android.scy.pictureclass;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvatarDiglog extends AppCompatActivity {
    private Uri imageUri;
    @BindView(R.id.avatar_btn_camera)
    Button avatarBtnCamera;
    @BindView(R.id.avatar_btn_album)
    Button avatarBtnAlbum;
    @BindView(R.id.avatar_btn_cancel)
    Button avatarBtnCancel;
    @BindView(R.id.activity_avatar_diglog)
    LinearLayout activityAvatarDiglog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_diglog);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.avatar_btn_camera, R.id.avatar_btn_album, R.id.avatar_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar_btn_camera:
                File output = new File(getExternalCacheDir(),"header.jpg");
                if(output.exists()){
                    output.delete();
                }
                try {
                    output.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(AvatarDiglog.this,"com.example.cameraalbumtest.fileprovider",output);
                }else {
                    imageUri = Uri.fromFile(output);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,10);
                break;
            case R.id.avatar_btn_album:
                if(ContextCompat.checkSelfPermission(AvatarDiglog.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AvatarDiglog.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                break;
            case R.id.avatar_btn_cancel:
                setResult(RESULT_CANCELED,null);
                finish();
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,20);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setResult(RESULT_CANCELED,null);
        finish();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"授权失败，未能打开相册",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode == RESULT_OK){
                    Intent reIn = new Intent();
                    reIn.putExtra("header",imageUri.toString());
                    reIn.putExtra("tag","camera");
                    setResult(RESULT_OK,reIn);
                    finish();
                }
                break;
            case 20:
                if(resultCode == RESULT_OK){
                    Log.d("xxxx","xasdasdasd");
                    handleImageOnOk(data);
                }
                break;
        }
    }

    private void handleImageOnOk(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath = uri.getPath();
        }
        Log.d("imgPath",imagePath);
        Intent reAlbum = new Intent();
        reAlbum.putExtra("tag","album");
        reAlbum.putExtra("imgPath",imagePath);
        setResult(RESULT_OK,reAlbum);
        finish();
    }

    public String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cur = getContentResolver().query(uri,null,selection,null,null);
        if(cur != null){
            if(cur.moveToFirst()){
                path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cur.close();
        }
        return path;
    }
}
//119.29.194.163/tp/UserInfo/upload   name=""
package com.reader.readingManagement.post.add.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

public class TempActivity extends AppCompatActivity {

    PickerManager pickerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.pickerManager = GlobalHolder.getInstance().getPickerManager();
        this.pickerManager.setActivity(TempActivity.this);
        this.pickerManager.pickPhotoWithPermission();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        switch (requestCode) {
            case PickerManager.REQUEST_CODE_SELECT_IMAGE:
                Uri uri;

                if (data != null) {
                    uri = data.getData();
                }
                else
                    uri = pickerManager.getImageFile();

                pickerManager.setUri(uri);

                // WRITE_EXTERNAL_STORAGE 퍼미션 체크...
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PickerManager.REQUEST_CODE_WRITE_PERMISSION);
                    }
                } else {
                    pickerManager.startCropActivity();
                }

                break;

            case PickerManager.REQUEST_CODE_WRITE_PERMISSION:

                break;

            // PostAddAcitivity에서 등록한 listener의 함수 실행!
            case REQUEST_CROP:
                if (data != null) {
                    pickerManager.handleCropResult(data);
                } else
                    finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PickerManager.REQUEST_CODE_IMAGE_PERMISSION)
                pickerManager.handlePermissionResult(grantResults);
        else if(requestCode == PickerManager.REQUEST_CODE_WRITE_PERMISSION) {
            //TODO : 이미지쓰기 퍼미션 핸들링
            pickerManager.startCropActivity();
        } else
            finish();

    }
/*
    public void checkWritePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "CHECK WRITE PERMISSION", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PickerManager.REQUEST_CODE_WRITE_PERMISSION);
            }
        } else {
            Toast.makeText(this, "CHECK WRITE PERMISSION", Toast.LENGTH_SHORT).show();
        }
    }*/



}

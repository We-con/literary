package com.reader.readingManagement.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naver on 2017. 2. 19..
 */

public class RuntimePermissions {

    public static interface OnPermissionResult {
        void onResult(int requestCode, boolean granted, String[] permissions);
    }

    /**
     * Id to identify a camera permission request.
     */
    public static final int ACTIVITY_REQUEST_DETAIL_SETUP = 8988;

    public static final int REQUEST_DEFAULT = 99;
    public static final int REQUEST_CAMERA = 0;
    public static final int REQUEST_STORAGE = 4;
    public static final int REQUEST_FILE_ATTACH = 5;

    public static final String[] REQUEST_NAME = {"default", "camera", "storage", "file_attach"};
    public static final boolean[] mNeverShow = {false, false, false, false};

    static long mRequestTS = 0;

    private static String[] PERMISSIONS_DEFAULT = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA};

    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static String[] PERMISSIONS_MIC = {Manifest.permission.RECORD_AUDIO};
    private static String[] PERMISSIONS_STORAGE = {/*Manifest.permission.READ_EXTERNAL_STORAGE,*/ Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static String[] PERMISSIONS_FILE_ATTACH = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    static Map<Integer, OnPermissionResult> mCallbackMap = new HashMap<Integer, OnPermissionResult>();
    static String PERMISION_GROUPS[][] = {PERMISSIONS_CAMERA, PERMISSIONS_CONTACT, PERMISSIONS_LOCATION, PERMISSIONS_MIC, PERMISSIONS_STORAGE, PERMISSIONS_FILE_ATTACH};

    // To check the permission granted
    static public boolean isGrantedDefault(Context context) {
        return isGrantedPermission(context, PERMISSIONS_DEFAULT);
    }

    static public boolean isGrantedCamera(Context context) {
        return isGrantedPermission(context, PERMISSIONS_CAMERA);
    }

    static public boolean isGrantedContact(Context context) {
        return isGrantedPermission(context, PERMISSIONS_CONTACT);
    }

    static public boolean isGrantedLocation(Context context) {
        return isGrantedPermission(context, PERMISSIONS_LOCATION);
    }

    static public boolean isGrantedMic(Context context) {
        return isGrantedPermission(context, PERMISSIONS_MIC);
    }

    static public boolean isGrantedStorage(Context context) {
        return isGrantedPermission(context, PERMISSIONS_STORAGE);
    }

    /* To check the permission request dialog shown */
    static public boolean isCameraRequestPending(Context context) {
        return mCallbackMap.get(REQUEST_CAMERA) != null;
    }

    //Permision Request
    static public boolean requestDefault(Activity activity, OnPermissionResult cb) {
        String[] permissions = getNotGrantedPermission(activity, PERMISSIONS_DEFAULT);
        if (permissions == null || permissions.length == 0) {
            return false;
        }

        return requestPermissions(activity, REQUEST_DEFAULT, permissions, cb);
    }

    static public boolean requestCamera(Activity activity, OnPermissionResult cb) {
        return requestPermissions(activity, REQUEST_CAMERA, PERMISSIONS_CAMERA, cb);
    }

    static public boolean requestContacts(Activity activity, OnPermissionResult cb) {
        return requestPermissions(activity, REQUEST_CAMERA, PERMISSIONS_CONTACT, cb);
    }


    public boolean requestPhoneState() {
        return false;
    }

    static public boolean requestStorage(Activity activity, OnPermissionResult cb) {
        return requestPermissions(activity, REQUEST_STORAGE, PERMISSIONS_STORAGE, cb);
    }

    static public boolean requestFileAttach(Activity activity, OnPermissionResult cb) {
        return requestPermissions(activity, REQUEST_FILE_ATTACH, PERMISSIONS_FILE_ATTACH, cb);
    }

    static public String[] getNotGrantedPermission(Context context, String[] permissions) {
        ArrayList<String> list = new ArrayList<String>(permissions.length);
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_DENIED) {
                list.add(p);
            }
        }
        return list.toArray(new String[0]);
    }

    static public boolean isGrantedPermission(Context context, String[] permissions) {
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    static public boolean isNeverShowAgain(Activity activity, int requestCode) {
        String[] permissions = PERMISION_GROUPS[requestCode];
        return isNeverShowAgain(activity, requestCode, permissions);
    }

    static public boolean isNeverShowAgain(Activity activity, int requestCode, String[] permissions) {
        //return isFirstRequest(activity,requestCode) == false && shouldShowRequestPermissionRationale(activity,permissions) == false;
        return mNeverShow[requestCode];
    }

    static public boolean requestPermissions(Activity activity, int requestCode, String[] permissions, OnPermissionResult cb) {
        if (isGrantedPermission(activity, permissions) == true) {
            cb.onResult(requestCode, true, permissions);
            return true;
        } else if (isNeverShowAgain(activity, requestCode, permissions) == true) {
            cb.onResult(requestCode, false, permissions);
            return true;
        }

        setRequestHistory(activity, requestCode);

        mRequestTS = System.currentTimeMillis();

        if (shouldShowRequestPermissionRationale(activity, permissions) == true) { //first Query or Never Ask Again
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }

        if (mCallbackMap.get(requestCode) != null) {
            return false;
        }
        mCallbackMap.put(requestCode, cb);
        return true;
    }


    static public boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, p) == true) {
                return true;
            }
        }
        return false;
    }

    static public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        OnPermissionResult cb = mCallbackMap.remove(requestCode);
        boolean granted = true;
        if (cb != null) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                }
            }
            long diff = System.currentTimeMillis() - mRequestTS;
            if (granted == false && diff < 200) { //Denyed
                mNeverShow[requestCode] = true;
            } else {
                mNeverShow[requestCode] = false;
            }

            cb.onResult(requestCode, granted, permissions);
        }
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void openAppDetailSettings(Context context, String packageName) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        if (packageName == null) {
            packageName = context.getPackageName();
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", packageName, null));
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, ACTIVITY_REQUEST_DETAIL_SETUP);
        } else {
            context.startActivity(intent);
        }
    }

    public static boolean isFirstRequest(Context context, int requestCode) {
        SharedPreferences pref = context.getSharedPreferences("permissions", 0);
        int count = pref.getInt(REQUEST_NAME[requestCode], 0);
        return count == 0;
    }

    public static void setRequestHistory(Context context, int requestCode) {
        SharedPreferences pref = context.getSharedPreferences("permissions", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(REQUEST_NAME[requestCode], 1);
        edit.commit();
    }


    public static void showDenyToast(Context context, int requstCode) {
        String message = "권한이 승인되지 않아 해당 장치를 사용할 수 없습니다.";
        switch (requstCode) {
            case REQUEST_CAMERA:
                message = "카메라에 대한 권한이 승인되지 않아 사진 촬영 기능은 사용할 수 없습니다.";
                break;
            case REQUEST_FILE_ATTACH:
                message = "일부 장치 권한이 승인되지 않아 사진촬영,녹음등 일부 기능은 제한 됩니다.";
                break;
            case REQUEST_STORAGE:
                message = "외장 메모리 저장 권한이 승인되지 않아 파일을 저장할 수 없습니다.";
                break;
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
